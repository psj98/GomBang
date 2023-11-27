package com.ssafy.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.global.common.response.BaseException;
import com.ssafy.global.common.response.BaseResponseStatus;
import com.ssafy.roomDeal.domain.RoomDealImageInfo;
import com.ssafy.roomDeal.repository.RoomDealImageInfoRepository;
import com.ssafy.showRoom.domain.ShowRoomImageInfo;
import com.ssafy.showRoom.repository.ShowRoomImageInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final RoomDealImageInfoRepository roomDealImageInfoRepository;
    private final ShowRoomImageInfoRepository showRoomImageInfoRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFiles(List<MultipartFile> files, Long roomDealId) throws BaseException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileUrl = putFile(file);
            fileUrls.add(fileUrl);
            saveFile(roomDealId, fileUrl);
        }

        return fileUrls;
    }

    public List<String> uploadFiles(List<MultipartFile> files, Integer showRoomId) throws BaseException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileUrl = putFile(file);
            fileUrls.add(fileUrl);
            saveFile(showRoomId, fileUrl);
        }

        return fileUrls;
    }

    private String putFile(MultipartFile file) throws BaseException{

        String fileName=file.getOriginalFilename();

        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
        } catch (IOException e) {
            throw new BaseException(BaseResponseStatus.PUT_FILE_FAILED);
        }

        return getFileUrl(fileName);

    }

    private String getFileUrl(String fileName) throws BaseException{

        String fileUrl;

        try {
            fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.GET_FILE_URL_FAILED);
        }

        return fileUrl;
    }

    @Transactional
    public Integer saveFile(Long roomDealId, String fileUrl) throws BaseException{

        RoomDealImageInfo roomDealImageInfo;

        try {
            roomDealImageInfo = roomDealImageInfoRepository.save(new RoomDealImageInfo(roomDealId, fileUrl));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.SAVE_FILE_FAILED);
        }

        return roomDealImageInfo.getId();
    }

    @Transactional
    public Integer saveFile(Integer showRoomId, String fileUrl) throws BaseException{

        ShowRoomImageInfo showRoomImageInfo;

        try {
            showRoomImageInfo = showRoomImageInfoRepository.save(new ShowRoomImageInfo(showRoomId, fileUrl));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.SAVE_FILE_FAILED);
        }

        return showRoomImageInfo.getId();
    }

}
