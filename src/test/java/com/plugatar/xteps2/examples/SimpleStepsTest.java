package com.plugatar.xteps2.examples;

import com.plugatar.xteps2.core.StepNotImplementedError;
import org.junit.jupiter.api.Test;

import static com.plugatar.xteps2.Keywords.AND;
import static com.plugatar.xteps2.Keywords.THEN;
import static com.plugatar.xteps2.Keywords.WHEN;
import static com.plugatar.xteps2.Params.params;
import static com.plugatar.xteps2.Steps.emptyStep;
import static com.plugatar.xteps2.Steps.step;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class SimpleStepsTest {

  @Test
  void emptySteps() {
    emptyStep("Step 1");
    emptyStep("Step 2");
    emptyStep("Step 3");
  }

  @Test
  void nestedSteps() {
    step("Step 1", () -> {
      step("Nested step 1", () -> {
        //...
      });
      step("Nested step 2", () -> {
        //...
      });
    });
    step("Step 2", () -> {
      //...
    });
  }

  @Test
  void stepsWithParamsAndDescription() {
    step("Step 1", params("param1", "value1", "param2", 123), () -> {
      //...
    });
    step("Step 2", "Description", () -> {
      //...
    });
    step("Step 3", params("param1", "value1", "param2", 123), () -> {
      //...
    });
  }

  @Test
  void stepsWithKeywords() {
    step(WHEN, "User do smth", () -> {
      //...
    });
    step(AND, "User do another action", () -> {
      //...
    });
    step(THEN, "User see smth", () -> {
      //...
    });
  }

  @Test
  void notImplementedSteps() {
    assertThatCode(() -> {
      emptyStep("Step 1");
    }).doesNotThrowAnyException();

    assertThatThrownBy(() -> {
      step("Step 2");
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step("Step 3", params("param1", "value1"));
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(WHEN, "Step 4");
    }).isInstanceOf(StepNotImplementedError.class);
  }
}
