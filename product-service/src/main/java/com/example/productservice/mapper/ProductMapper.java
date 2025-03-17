//package com.example.productservice.mapper;
//
//import com.example.productservice.dto.ProductDTO;
//import com.example.productservice.entity.Product;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProductMapper extends AbstractMapper<Product, ProductDTO> {
//    @Autowired
//    private ProductDetailMapper productDetailMapper;
//
//    @Autowired
//    private CommentMapper commentMapper;
//
//    @Override
//    public Class<ProductDTO> getDtoClass() {
//        return ProductDTO.class;
//    }
//
//    @Override
//    public Class<Product> getEntityClass() {
//        return Product.class;
//    }
//}
