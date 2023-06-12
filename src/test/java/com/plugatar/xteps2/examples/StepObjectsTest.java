package com.plugatar.xteps2.examples;

import com.plugatar.xteps2.core.StepNotImplementedError;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.step.BiConsumerStep;
import com.plugatar.xteps2.core.step.BiFunctionStep;
import com.plugatar.xteps2.core.step.ConsumerStep;
import com.plugatar.xteps2.core.step.FunctionStep;
import com.plugatar.xteps2.core.step.RunnableStep;
import com.plugatar.xteps2.core.step.SupplierStep;
import com.plugatar.xteps2.core.step.TriConsumerStep;
import com.plugatar.xteps2.core.step.TriFunctionStep;
import org.junit.jupiter.api.Test;

import static com.plugatar.xteps2.Keywords.and;
import static com.plugatar.xteps2.Keywords.then;
import static com.plugatar.xteps2.Keywords.when;
import static com.plugatar.xteps2.Params.params;
import static com.plugatar.xteps2.Steps.step;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class StepObjectsTest {

  @Test
  void runnableStep() {
    RunnableStep runnableStep = new RunnableStep.Of("Runnable step", () -> {
      //...
    });

    step(runnableStep);
  }

  @Test
  void supplierStep() {
    SupplierStep<String> supplierStep = new SupplierStep.Of<>("Supplier step", () -> {
      //...
      return "step result";
    });

    String stepResult = step(supplierStep);
  }

  @Test
  void consumerSteps() {
    ConsumerStep<Integer> consumerStep = new ConsumerStep.Of<>("Consumer step", integer -> {
      //...
    });
    BiConsumerStep<Integer, String> biConsumerStep = new BiConsumerStep.Of<>("BiConsumer step", (integer, str) -> {
      //...
    });
    TriConsumerStep<Integer, String, Double> triConsumerStep = new TriConsumerStep.Of<>("TriConsumer step", (integer, str, d) -> {
      //...
    });

    step(consumerStep, 123);
    step(biConsumerStep, 123, "value");
    step(triConsumerStep, 123, "value", 124.5);
  }

  @Test
  void functionSteps() {
    FunctionStep<Integer, String> functionStep = new FunctionStep.Of<>("Function step", integer -> {
      //...
      return "step result";
    });
    BiFunctionStep<Integer, String, String> biFunctionStep = new BiFunctionStep.Of<>("BiFunction step", (integer, str) -> {
      //...
      return "step result";
    });
    TriFunctionStep<Integer, String, Double, String> triFunctionStep = new TriFunctionStep.Of<>("TriFunction step", (integer, str, d) -> {
      //...
      return "step result";
    });

    String step1Result = step(functionStep, 123);
    String step2Result = step(biFunctionStep, 123, "value");
    String step3Result = step(triFunctionStep, 123, "value", 124.5);
  }

  @Test
  void nestedSteps() {
    RunnableStep innerStep1 = new RunnableStep.Of("Inner step 1", () -> {
      //...
    });
    RunnableStep innerStep2 = new RunnableStep.Of("Inner step 2", () -> {
      //...
    });
    RunnableStep step1 = new RunnableStep.Of("Step 1", () -> {
      step(innerStep1);
      step(innerStep2);
    });
    RunnableStep step2 = new RunnableStep.Of("Step 2", () -> {
      //...
    });

    step(step1);
    step(step2);
  }

  @Test
  void stepsWithParamsAndDescription() {
    RunnableStep step1 = new RunnableStep.Of("Step 1", params("param1", "value1", "param2", 123), () -> {
      //...
    });
    RunnableStep step2 = new RunnableStep.Of("Step 2", "Description", () -> {
      //...
    });
    RunnableStep step3 = new RunnableStep.Of("Step 3", params("param1", "value1", "param2", 123), "Description", () -> {
      //...
    });

    step(step1);
    step(step2);
    step(step3);
  }

  @Test
  void stepsWithKeywords() {
    RunnableStep step1 = new RunnableStep.Of("User do smth", () -> {
      //...
    });
    RunnableStep step2 = new RunnableStep.Of("User do another action", () -> {
      //...
    });
    RunnableStep step3 = new RunnableStep.Of("User see smth", () -> {
      //...
    });

    step(when(step1));
    step(and(step2));
    step(then(step3));
  }

  @Test
  void anonymousClassImplStep() {
    RunnableStep anonymousClassImplStep = new RunnableStep.Of("Anonymous class impl step", new ThRunnable<Throwable>() {
      int counter = 0;

      @Override
      public void run() throws Throwable {
        counter++;
      }
    });

    step(anonymousClassImplStep);
  }

  @Test
  void notImplementedSteps() {
    RunnableStep runnableStep = new RunnableStep.Of("Runnable step");
    SupplierStep<String> supplierStep = new SupplierStep.Of<>("Supplier step");
    ConsumerStep<Integer> consumerStep = new ConsumerStep.Of<>("Consumer step");
    BiConsumerStep<Integer, String> biConsumerStep = new BiConsumerStep.Of<>("BiConsumer step");
    TriConsumerStep<Integer, String, Double> triConsumerStep = new TriConsumerStep.Of<>("TriConsumer step");
    FunctionStep<Integer, String> functionStep = new FunctionStep.Of<>("Function step");
    BiFunctionStep<Integer, String, String> biFunctionStep = new BiFunctionStep.Of<>("BiFunction step");
    TriFunctionStep<Integer, String, Double, String> triFunctionStep = new TriFunctionStep.Of<>("TriFunction step");

    assertThatThrownBy(() -> {
      step(runnableStep);
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(supplierStep);
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(consumerStep, 123);
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(biConsumerStep, 123, "value");
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(triConsumerStep, 123, "value", 124.5);
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(functionStep, 123);
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(biFunctionStep, 123, "value");
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      step(triFunctionStep, 123, "value", 124.5);
    }).isInstanceOf(StepNotImplementedError.class);
  }
}
