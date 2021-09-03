package com.tutor.tutorlab.modules.file.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.file.enums.FileType;
import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "file_id"))
@Table(name = "tutorlab_file")
@Entity
public class File extends BaseEntity {

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "type", length = 50)
    private FileType type;

    @Column(name = "name")
    private String name;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size")
    private Long size;

}
