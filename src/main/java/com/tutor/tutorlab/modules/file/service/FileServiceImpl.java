package com.tutor.tutorlab.modules.file.service;

import com.tutor.tutorlab.modules.file.mapstruct.FileMapstruct;
import com.tutor.tutorlab.modules.file.repository.FileRepository;
import com.tutor.tutorlab.modules.file.response.FileResponse;
import com.tutor.tutorlab.modules.file.vo.File;
import com.tutor.tutorlab.modules.upload.service.dto.AddFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapstruct fileMapstruct;

    @Transactional
    @Override
    public FileResponse add(AddFile addFile) {
        File file = fileMapstruct.addFileToFile(addFile);
        return fileMapstruct.fileToFileResponse(fileRepository.save(file));
    }

    @Override
    public FileResponse get(String uuid) {
        File file = fileRepository.findByUuid(uuid);
        return fileMapstruct.fileToFileResponse(file);
    }
}
