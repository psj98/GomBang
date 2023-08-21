import React, { useEffect } from "react";
import Header from "../Header";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import styles from "./RtcRoom.module.css";
import axios from "axios";
import ChatRoomComponent from "../Chatting/ChatRoomComponent";

const GrantorRtcRoom = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { id, roomDealId } = useParams();

  var socket;

  //   const exitButton = document.querySelector("#exit");
  var localVideo = document.getElementById("local_video");
  var localUserName;

  useEffect(() => {
    const member = JSON.parse(sessionStorage.getItem("member"));
    localVideo = document.getElementById("local_video");
    localUserName = member.id;
    socket = new WebSocket(`${process.env.REACT_APP_WS_URL}/signal`);

    start();
  }, []);

  useEffect(() => {
    return () => {
      stop();
    };
  }, [location]);

  // WebRTC 관련 코드 -------------------------------------------------------
  // WebRTC STUN servers
  // WebRTC STUN 서버 정보
  const peerConnectionConfig = {
    iceServers: [
      { urls: "stun:stun.stunprotocol.org:3478" },
      { urls: "stun:stun.l.google.com:19302" },
    ],
  };

  // WebRTC media
  const mediaConstraints = {
    video: true,
    audio: {
      echoCancellation: true,
      noiseSuppression: true,
      sampleRate: 44100,
    },
  };

  // WebRTC variables
  let localStream;
  let localVideoTracks;
  let myPeerConnection;

  async function start() {
    await createLiveRoom();
    // 페이지 시작시 실행되는 메서드 -> socket 을 통해 server 와 통신한다
    socket.onmessage = async function (msg) {
      let message = JSON.parse(msg.data);

      switch (message.type) {
        case "offer":
          console.log("Signal OFFER received");
          handleOfferMessage(message);
          break;

        case "answer":
          console.log("Signal ANSWER received");
          handleAnswerMessage(message);
          break;

        case "ice":
          console.log("Signal ICE Candidate received");
          handleNewICECandidateMessage(message);
          break;

        case "join":
          // ajax 요청을 보내서 userList 를 다시 확인함
          //   message.data = await chatListCount();
          axios
            .get(`${process.env.REACT_APP_API_ROOT}/rtc/usercount/${id}`)
            .then((response) => {
              console.log(response.data.data.overOne);
              message.data = response.data.data.overOne;
              return message;
            })
            .then((message) => {
              handlePeerConnection(message);
            })
            .catch((error) => {
              console.log("오류:", error);
            });
          break;

        case "leave":
          stop();
          break;

        default:
          handleErrorMessage("Wrong type message received from server");
      }
    };

    // ICE 를 위한 chatList 인원 확인
    function createLiveRoom() {
      axios
        .post(`${process.env.REACT_APP_API_ROOT}/rtc/create`, {
          roomId: `${id}`,
        })
        .then((response) => {
          //   console.log(response);
        })
        .catch((error) => {
          console.log("오류:", error);
        });
    }

    // add an event listener to get to know when a connection is open
    // 웹 소켓 연결 되었을 때 - open - 상태일때 이벤트 처리
    socket.onopen = function () {
      console.log("WebSocket connection opened to Room: #" + id);
      // send a message to the server to join selected room with Web Socket
      sendToServer({
        from: localUserName,
        type: "join",
        data: id,
      });
    };

    // a listener for the socket being closed event
    // 소켓이 끊겼을 때 이벤트처리
    socket.onclose = function (message) {
      console.log("Socket has been closed");
    };

    // an event listener to handle socket errors
    // 에러 발생 시 이벤트 처리
    socket.onerror = function (message) {
      handleErrorMessage("Error: " + message);
    };
  }

  // 브라우저 종료 시 이벤트
  // 그냥...브라우저 종료 시 stop 함수를 부르면 된다ㅠㅠ
  window.addEventListener("unload", stop);

  // 브라우저 뒤로가기 시 이벤트
  window.onhashchange = function () {
    stop();
  };

  function stopToServer(msg) {
    let msgJSON = JSON.stringify(msg);
    socket.send(msgJSON);
    return new Promise((resolve, reject) => resolve(1));
  }

  function stop() {
    // send a message to the server to remove this client from the room clients list
    log("Send 'leave' message to server");
    stopToServer({
      from: localUserName,
      type: "leave",
      data: id,
    }).then((response) => {
      if (myPeerConnection) {
        log("Close the RTCPeerConnection");

        // disconnect all our event listeners
        myPeerConnection.onicecandidate = null;
        myPeerConnection.ontrack = null;
        myPeerConnection.onnegotiationneeded = null;
        myPeerConnection.oniceconnectionstatechange = null;
        myPeerConnection.onsignalingstatechange = null;
        myPeerConnection.onicegatheringstatechange = null;
        myPeerConnection.onnotificationneeded = null;
        myPeerConnection.onremovetrack = null;

        // Stop the videos
        // 비디오 정지
        if (localVideo.srcObject) {
          localVideo.srcObject.getTracks().forEach((track) => track.stop());
        }

        localVideo.src = null;

        // close the peer connection
        // myPeerConnection 초기화
        myPeerConnection.close();
        myPeerConnection = null;

        closeSocket();
      }
    });
  }

  async function closeSocket() {
    log("Close the socket");
    if (socket != null) {
      socket.close();
    }
  }

  // room exit button handler
  function exitLive() {
    stop();
    navigate(-1);
  }

  function log(message) {
    console.log(message);
  }

  function handleErrorMessage(message) {
    console.error(message);
  }

  // use JSON format to send WebSocket message
  function sendToServer(msg) {
    let msgJSON = JSON.stringify(msg);
    socket.send(msgJSON);
  }

  // initialize media stream
  async function getMedia(constraints) {
    if (localStream) {
      localStream.getTracks().forEach((track) => {
        track.stop();
      });
    }

    let mediaStream = await navigator.mediaDevices.getUserMedia(constraints);
    try {
      getLocalMediaStream(mediaStream);
    } catch (error) {
      handleGetUserMediaError(error);
    }

    // navigator.mediaDevices
    //   .getUserMedia(constraints)
    //   .then(getLocalMediaStream)
    //   .catch(handleGetUserMediaError);
  }

  // create peer connection, get media, start negotiating when second participant appears
  // 두번째 클라이언트가 들어오면 피어 연결을 생성 + 미디어 생성
  function handlePeerConnection(message) {
    createPeerConnection();

    getMedia(mediaConstraints);

    console.log(typeof message.data);

    if (message.data === true) {
      myPeerConnection.onnegotiationneeded = handleNegotiationNeededEvent;
    }
  }

  function createPeerConnection() {
    myPeerConnection = new RTCPeerConnection(peerConnectionConfig);

    // event handlers for the ICE negotiation process
    myPeerConnection.onicecandidate = handleICECandidateEvent;
    myPeerConnection.ontrack = handleTrackEvent;

    // the following events are optional and could be realized later if needed
    // myPeerConnection.onremovetrack = handleRemoveTrackEvent;

    myPeerConnection.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
    // myPeerConnection.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
    // myPeerConnection.onsignalingstatechange = handleSignalingStateChangeEvent;
  }

  /** peerConnection 과 관련된 이벤트 처리
   * 다른 peer 와 연결되었을 때 remote_video show 상태로로, 끊졌을때는 remote_video 를 hide 상태로 변경
   * **/
  function handleICEConnectionStateChangeEvent() {
    let status = myPeerConnection.iceConnectionState;
    console.log(status);

    if (status === "connected") {
      log("status : " + status);
    } else if (status === "disconnected") {
      log("status : " + status);
    }
  }

  // add MediaStream to local video element and to the Peer
  async function getLocalMediaStream(mediaStream) {
    localVideo = document.getElementById("local_video");
    localStream = mediaStream;
    localVideo.srcObject = mediaStream;
    localStream.getTracks().forEach((track) => myPeerConnection.addTrack(track, localStream));
  }

  // handle get media error
  async function handleGetUserMediaError(error) {
    log("navigator.getUserMedia error: ", error);
    switch (error.name) {
      case "NotFoundError":
        alert("Unable to open your call because no camera and/or microphone were found.");
        break;
      case "SecurityError":
      case "PermissionDeniedError":
        // Do nothing; this is the same as the user canceling the call.
        break;
      default:
        alert("Error opening your camera and/or microphone: " + error.message);
        break;
    }

    stop();
  }

  // send ICE candidate to the peer through the server
  function handleICECandidateEvent(event) {
    if (event.candidate) {
      sendToServer({
        from: localUserName,
        data: id,
        type: "ice",
        candidate: event.candidate,
      });
      log("ICE Candidate Event: ICE candidate sent");
    }
  }

  function handleTrackEvent(event) {
    log("Track Event: set stream to remote video element");
  }

  // WebRTC called handler to begin ICE negotiation
  // WebRTC 의 ICE 통신 순서
  // 1. WebRTC offer 생성
  // 2. local media description 생성?
  // 3. 미디어 형식, 해상도 등에 대한 내용을 서버에 전달
  function handleNegotiationNeededEvent() {
    myPeerConnection
      .createOffer()
      .then(function (offer) {
        return myPeerConnection.setLocalDescription(offer);
      })
      .then(function () {
        sendToServer({
          from: localUserName,
          data: id,
          type: "offer",
          sdp: myPeerConnection.localDescription,
        });
        log("Negotiation Needed Event: SDP offer sent");
      })
      .catch(function (reason) {
        // an error occurred, so handle the failure to connect
        handleErrorMessage("failure to connect error: ", reason);
      });
  }

  function handleOfferMessage(message) {
    log("Accepting Offer Message");
    log(message);
    let desc = new RTCSessionDescription(message.sdp);
    //TODO test this
    if (desc != null && message.sdp != null) {
      log("RTC Signalling state: " + myPeerConnection.signalingState);
      myPeerConnection
        .setRemoteDescription(desc)
        .then(function () {
          log("Set up local media stream");
          return navigator.mediaDevices.getUserMedia(mediaConstraints);
        })
        .then(function (stream) {
          log("-- Local video stream obtained");
          localStream = stream;
          try {
            localVideo.srcObject = localStream;
          } catch (error) {
            localVideo.src = window.URL.createObjectURL(stream);
          }

          log("-- Adding stream to the RTCPeerConnection");
          localStream.getTracks().forEach((track) => myPeerConnection.addTrack(track, localStream));
        })
        .then(function () {
          log("-- Creating answer");
          // Now that we've successfully set the remote description, we need to
          // start our stream up locally then create an SDP answer. This SDP
          // data describes the local end of our call, including the codec
          // information, options agreed upon, and so forth.
          return myPeerConnection.createAnswer();
        })
        .then(function (answer) {
          log("-- Setting local description after creating answer");
          // We now have our answer, so establish that as the local description.
          // This actually configures our end of the call to match the settings
          // specified in the SDP.
          return myPeerConnection.setLocalDescription(answer);
        })
        .then(function () {
          log("Sending answer packet back to other peer");

          sendToServer({
            from: localUserName,
            data: id,
            type: "answer",
            sdp: myPeerConnection.localDescription,
          });
        })
        // .catch(handleGetUserMediaError);
        .catch(handleErrorMessage);
    }
  }

  function handleAnswerMessage(message) {
    log("The peer has accepted request");

    // Configure the remote description, which is the SDP payload
    // in our "video-answer" message.
    // myPeerConnection.setRemoteDescription(new RTCSessionDescription(message.sdp)).catch(handleErrorMessage);
    myPeerConnection.setRemoteDescription(message.sdp).catch(handleErrorMessage);
  }

  function handleNewICECandidateMessage(message) {
    let candidate = new RTCIceCandidate(message.candidate);
    log("Adding received ICE candidate: " + JSON.stringify(candidate));
    myPeerConnection.addIceCandidate(candidate).catch(handleErrorMessage);
  }

  // -------------------------------------------------------------------------

  return (
    <div>
      <button
        type="button"
        className="btn btn-outline-danger"
        id="exit"
        name="exit"
        onClick={() => exitLive()}
      >
        Exit Room
      </button>
      <div className={styles.flexContainer}>
        <div className={styles.videoArea}>
          <video id="local_video" className={styles.videoElem} autoPlay playsInline></video>
        </div>
        <div className={styles.chatArea}>
          <ChatRoomComponent id={id} roomDealId={roomDealId} />
        </div>
      </div>
    </div>
  );
};

export default GrantorRtcRoom;
