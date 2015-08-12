package demo.config.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VersionValidatorTest {

	private Validator validator;

	@Before
	public void setup() {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		this.validator = vf.getValidator();
	}

	@Test
	public void validateNullVersion() {
		testValidation(new SampleBean(null), true); // TODO
	}

	@Test
	public void validateSimpleVersion() {
		testValidation(new SampleBean("1.2.0.RELEASE"), true);
	}

	@Test
	public void validateNoQualifierVersion() {
		testValidation(new SampleBean("1.5.1"), true);
	}

	@Test
	public void validateRubbish() {
		testValidation(new SampleBean("OneDotThree"), false);
	}

	private void testValidation(SampleBean bean, boolean valid) {
		Set<ConstraintViolation<SampleBean>> violations = this.validator.validate(bean);
		assertEquals(valid ? 0 : 1, violations.size());
	}


	private static class SampleBean {

		@Version
		private String version;

		public SampleBean(String version) {
			this.version = version;
		}

	}
}
