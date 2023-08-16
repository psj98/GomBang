import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";
import QR from "qrcode.react";

const RtcRoomQR = () => {
  const { isGrantor, id, roomDealId } = useParams();

  return (
    <div>
      <QR
        value={`https://i9a804.p.ssafy.io/rtcroom/grantor/${id}/${roomDealId}`}
        size={500}
        id="qr-gen"
        level={"H"}
        includeMargin={true}
      />
    </div>
  );
};

export default RtcRoomQR;
