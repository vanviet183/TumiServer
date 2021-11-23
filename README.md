# Tumi server with spring boot
### Công nghệ and tool:

- Cloudinary: upload ảnh
- Spring security
- ModelMapper: convert object
- Lombok
- Send mail: đăng ký
- Swagger: http://localhost:8080/swagger-ui.html
- Slugify: https://github.com/slugify/slugify
- Gửi mail chúc mừng sinh nhật vào ngày sinh nhật của user
- Schedule: Nếu 1 ngày 1 đăng nhập web thì sẽ check vào 21h và gửi mail nhắc vào học.
- Giới hạn request forgot password 2 lần / 1p xử dụng: bucket4j-core.
- Export / Import excel file.
- Tự động backup data vào 00:00 ngày 15 mỗi tháng(0 0 0 15 * ?), nếu lỗi thì thông báo cho admin.
- Upload file vào Database hoặc project
- Download file từ Database hoặc project

## Note đang làm:

- Thêm chức năng: tính thời gian online 1 ngày, tổng 1 tháng. (xong)
- Tính tổng thời gian 1 tuần: Chưa làm
- Auto backup data (xong)
- Online 1 ngày có mức giờ và nhận quà theo giờ online.
- Thêm chức năng nhận quà mỗi ngày như game VLCM :v
A