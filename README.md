# Tumi server with spring boot
### Tính năng:

- Cloudinary: upload ảnh
- Spring security
- ModelMapper: convert object
- Send mail: đăng ký
- Swagger: http://localhost:8080/swagger-ui.html
- Slugify: https://github.com/slugify/slugify
- Gửi mail chúc mừng sinh nhật vào ngày sinh nhật của user, tặng một món quà ngẫu nhiên và gửi thông báo đến user.
- Schedule: Nếu 1 ngày 1 đăng nhập web thì sẽ check vào 21h và gửi mail nhắc vào học.
- Giới hạn request forgot password 2 lần / 1p xử dụng: bucket4j-core.
- Export / Import excel file.
- Tự động backup data vào 00:00 ngày 15 mỗi tháng(0 0 0 15 * ?), nếu lỗi thì thông báo cho admin.
- Upload file vào Database hoặc project
- Download file từ Database hoặc project
- OAuth2: hỗ trợ đăng nhập với github và google
- Sử dụng Data Redis cho service
