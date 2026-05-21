package com.library.mapper;

import com.library.dto.request.*;
import com.library.dto.response.*;
import com.library.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KunapiyaNurkamalMapper {

    KunapiyaNurkamalMapper INSTANCE = Mappers.getMapper(KunapiyaNurkamalMapper.class);

    // === Book Mappings ===
    KunapiyaNurkamalBook toBook(KunapiyaNurkamalBookRequest request);

    @Mapping(target = "authorName", expression = "java(book.getAuthor() != null ? book.getAuthor().getFirstName() + \" \" + book.getAuthor().getLastName() : null)")
    @Mapping(target = "categoryName", expression = "java(book.getCategory() != null ? book.getCategory().getName() : null)")
    KunapiyaNurkamalBookResponse toBookResponse(KunapiyaNurkamalBook book);

    List<KunapiyaNurkamalBookResponse> toBookResponseList(List<KunapiyaNurkamalBook> books);

    // === Author Mappings ===
    KunapiyaNurkamalAuthor toAuthor(KunapiyaNurkamalAuthorRequest request);

    @Mapping(target = "bookCount", expression = "java(author.getBooks() != null ? author.getBooks().size() : 0)")
    KunapiyaNurkamalAuthorResponse toAuthorResponse(KunapiyaNurkamalAuthor author);

    List<KunapiyaNurkamalAuthorResponse> toAuthorResponseList(List<KunapiyaNurkamalAuthor> authors);

    // === Category Mappings ===
    KunapiyaNurkamalCategory toCategory(KunapiyaNurkamalCategoryRequest request);

    @Mapping(target = "bookCount", expression = "java(category.getBooks() != null ? category.getBooks().size() : 0)")
    KunapiyaNurkamalCategoryResponse toCategoryResponse(KunapiyaNurkamalCategory category);

    List<KunapiyaNurkamalCategoryResponse> toCategoryResponseList(List<KunapiyaNurkamalCategory> categories);

    // === BorrowRecord Mappings ===
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "bookIsbn", source = "book.isbn")
    @Mapping(target = "status", expression = "java(record.getStatus().name())")
    KunapiyaNurkamalBorrowResponse toBorrowResponse(KunapiyaNurkamalBorrowRecord record);

    List<KunapiyaNurkamalBorrowResponse> toBorrowResponseList(List<KunapiyaNurkamalBorrowRecord> records);

    // === Fine Mappings ===
    @Mapping(target = "borrowRecordId", source = "borrowRecord.id")
    @Mapping(target = "username", source = "borrowRecord.user.username")
    @Mapping(target = "bookTitle", source = "borrowRecord.book.title")
    KunapiyaNurkamalFineResponse toFineResponse(KunapiyaNurkamalFine fine);

    List<KunapiyaNurkamalFineResponse> toFineResponseList(List<KunapiyaNurkamalFine> fines);
}