package com.example.tumiweb.domain.entity;

import com.example.tumiweb.application.constants.TableNameConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name = TableNameConstant.TBL_DB_FILE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DBFile {

  @Id
  //Ex ID with String
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @Nationalized
  private String fileName;

  private String fileType;

  @Lob
  private byte[] data;

  private Long uploadBy;

  private String pathDownload;

  public DBFile(String fileName, String fileType, byte[] data, Long uploadBy) {
    this.fileName = fileName;
    this.fileType = fileType;
    this.data = data;
    this.uploadBy = uploadBy;
  }

    /*
    * @Lob: được sử dụng để chỉ định rằng thuộc tính thực thể được chú thích hiện tại đại diện cho một loại đối tượng
    *  lớn.
    * Kiểu dữ liệu có hai biến thể:
        CLOB - Đối tượng lớn ký tự sẽ lưu trữ dữ liệu văn bản lớn
        BLOB - Đối tượng lớn nhị phân dùng để lưu trữ dữ liệu nhị phân như hình ảnh, âm thanh hoặc video
    * */
}