package com.madlim.junit.student;

import com.madlim.junit.student.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    //Mock annotation will be used when we dont want to test the repository since we know the repository already works
    //It will reduce resource to verify the db connection and so on
    @Mock
    private StudentRepository studentRepository;

    private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp(){
        //Initialise the studentRepository since it is annoted with @Mock
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        // it will close the resource after test completed
        autoCloseable.close();
    }

    @Test
    void canGetAllStudents() {
        //given

        //when
        underTest.getAllStudents();

        //then
        verify(studentRepository).findAll();

    }

    @Test
    void canAddStudent() {
        //given
        String email = "Test@gmail.com";
        Student student = new Student("Jamila", email, Gender.FEMALE);

        //when
        underTest.addStudent(student);

        //then
        //ArgumentCaptor used to capture argument from the the service class where student is take as a argument for this function
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    @Disabled // Disable this test
    void deleteStudent() {
    }

    @Test
    void willItThrowWhenEmailIsTaken () {
        //given
        String email = "Test@gmail.com";
        Student student = new Student("Jamila", email, Gender.FEMALE);

        given(studentRepository.selectExistsEmail(anyString())).willReturn(true);

        //then
        assertThatThrownBy(() -> {
            underTest.addStudent(student);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any()); //dont save any student to DB

    }
}