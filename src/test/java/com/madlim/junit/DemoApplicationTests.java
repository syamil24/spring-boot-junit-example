package com.madlim.junit;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DemoApplicationTests {

	Calculator underTest = new Calculator();

	@Test
	void itShouldAddTwoNumbers() {
		int numberOne = 20;
		int numberTwo = 30;

		//when
		int result = underTest.add(numberOne,numberTwo);

		//then
		assertThat(result).isEqualTo(50);
	}

	class Calculator {
		int add(int a, int b) {
			return a + b;
		}
	}

}
