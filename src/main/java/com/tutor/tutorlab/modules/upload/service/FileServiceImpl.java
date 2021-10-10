package com.tutor.tutorlab.modules.upload.service;

import com.tutor.tutorlab.modules.upload.controller.response.FileResponse;
import com.tutor.tutorlab.modules.upload.respository.FileRepository;
import com.tutor.tutorlab.modules.upload.service.request.FileRequest;
import com.tutor.tutorlab.modules.upload.vo.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public FileResponse createFile(FileRequest fileRequest) {
        File file = File.of(
                fileRequest.getUuid(),
                fileRequest.getType(),
                fileRequest.getName(),
                fileRequest.getContentType(),
                fileRequest.getSize()
        );
        return new FileResponse(fileRepository.save(file));
    }

    @Transactional(readOnly = true)
    @Override
    public FileResponse getFile(String uuid) {
        File file = fileRepository.findByUuid(uuid);
        return new FileResponse(file);
    }
}
