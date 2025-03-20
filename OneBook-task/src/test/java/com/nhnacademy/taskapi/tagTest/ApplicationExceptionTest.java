//package com.nhnacademy.taskapi.tagTest;
//
//import com.nhnacademy.taskapi.Tag.exception.ApplicationException;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ApplicationExceptionTest {
//
//    @Test
//    void testApplicationException() {
//        ApplicationException exception = new ApplicationException("에러 메시지", 500);
//        assertEquals("에러 메시지", exception.getErrorResponse().getTitle());
//    }
//}