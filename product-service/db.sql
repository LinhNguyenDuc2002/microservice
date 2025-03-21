CREATE TABLE address (
    id VARCHAR(255) PRIMARY KEY,
    detail VARCHAR(255),
    ward VARCHAR(255),
    district VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    created_date DATE,
    last_modified_date DATE
);

INSERT INTO category(id, name) VALUES('afac4c93-a175-4210-8dcb-64c5a4c0352d', 'Điện thoại - Máy tính bảng');
INSERT INTO category(id, name) VALUES('d1e6f3a3-9394-4c7c-9428-ecb215b93e59', 'TV');
INSERT INTO category(id, name) VALUES('c53a4ea0-8f4a-42e8-b9be-e424eb23a5ae', 'Điện gia dụng');
INSERT INTO category(id, name) VALUES('4e887ee3-ff3c-42d8-a0f8-be81fcbb4bf0', 'Văn phòng phẩm');
INSERT INTO category(id, name) VALUES('839ee0f1-1121-4b3f-ae40-2ef3bc5862f7', 'Đồ dùng học tập');
INSERT INTO category(id, name) VALUES('8badc990-886b-4227-99ab-ca5659cce6be', 'Thời trang nam');
INSERT INTO category(id, name) VALUES('50d09004-e762-463a-b2d4-40174c44f947', 'Thời trang nữ');
INSERT INTO category(id, name) VALUES('9fee7378-8f9c-4865-8cca-bcb9281faff1', 'Phụ kiện & Trang sức nam');
INSERT INTO category(id, name) VALUES('f3d205a1-374e-488a-b538-fe5ae52652b7', 'Phụ kiện & Trang sức nữ');
INSERT INTO category(id, name) VALUES('322a162e-965e-4ead-96ee-5fcf5c06780e', 'Làm đẹp - Sức khỏe');
INSERT INTO category(id, name) VALUES('88988a5c-fbe0-4d20-a363-f6dfbaee725e', 'Sách - truyện');
INSERT INTO category(id, name) VALUES('d74c2456-a9fb-435e-9dcf-ce0c136ea990', 'Đồ hand-made');
INSERT INTO category(id, name) VALUES('67ea3888-1071-4355-b147-57f2cfc1a2f5', 'Ô tô - Xe máy - Xe đạp');
INSERT INTO category(id, name) VALUES('9b087e74-1f87-41c2-998c-be7e30493027', 'Nhà cửa & đời sống');
INSERT INTO category(id, name) VALUES('7a9a4d90-6b38-49c8-8729-a01eefed54d0', 'Thiết bị số - Phụ kiện số');
INSERT INTO category(id, name) VALUES('ed1feab7-409f-4701-9793-0f5e390094f2', 'Thể thao & Du lịch');
INSERT INTO category(id, name) VALUES('9edc3529-2926-4032-b9a1-e94cc8c35d12', 'Đồ ăn vặt');
INSERT INTO category(id, name) VALUES('d6f095c8-e07f-4c13-8886-5bab78031512', 'Thời trang trẻ em');
INSERT INTO category(id, name) VALUES('d9ad2068-a488-4ca0-bec0-31827395a9f9', 'Đồ dùng & Thiết bị văn phòng');
INSERT INTO category(id, name) VALUES('ddf88122-63d8-4216-9378-311f27a054b4', 'Sex toy');
INSERT INTO category(id, name) VALUES('e1a5417b-9c7a-4fbb-9bb0-14889d053d37', 'Đồ chơi - Mẹ & bé');
INSERT INTO category(id, name) VALUES('e689fbd3-4c47-4507-ba72-ad550f34b00c', 'Điện tử - Điện lạnh');

SELECT * FROM product_service.feature WHERE id IN (
    SELECT feature_id FROM product_service.attribute WHERE id IN (
        SELECT attribute_id FROM product_service.product_attribute WHERE product_type_id IN (
            SELECT id FROM product_service.product_type WHERE product_id = "63187ed4-b674-47f1-a388-32af473ac204"
        )
    )
);

SELECT DISTINCT f.* FROM product_service.feature f
JOIN product_service.attribute a ON a.feature_id = f.id
JOIN product_service.product_attribute pa ON pa.attribute_id = a.id
JOIN product_service.product_type pt ON pt.id = pa.product_type_id
WHERE pt.product_id = '1161b5eb-fe02-40a1-8c34-3028417d5e70';