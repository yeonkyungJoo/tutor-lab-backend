package com.tutor.tutorlab.modules.file.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.file.enums.FileType;
import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "file_id"))
@Table(name = "tutorlab_file")
@Entity
public class File extends BaseEntity {

    private String uuid;

    @Column(length = 50)
    private FileType type;

    private String name;

    private String contentType;

    private Long size;

}
