package com.plugatar.xteps2.examples;

import com.plugatar.xteps2.core.step.BiConsumerStep;
import com.plugatar.xteps2.core.step.ConsumerStep;
import com.plugatar.xteps2.core.step.RunnableStep;
import com.plugatar.xteps2.core.step.SupplierStep;
import org.junit.jupiter.api.Test;

import static com.plugatar.xteps2.StepContexts.stepContext;
import static com.plugatar.xteps2.Steps.step;

final class StepContextsTest {

  @Test
  void stepContexts() {
    RunnableStep runnableStep = new RunnableStep.Of("Runnable step", () -> {
      //...
    });
    SupplierStep<String> strSupplierStep = new SupplierStep.Of<>("String supplier step", () -> {
      //...
      return "step result";
    });
    SupplierStep<Integer> intSupplierStep = new SupplierStep.Of<>("Integer supplier step", () -> {
      //...
      return 123;
    });
    ConsumerStep<String> strConsumerStep = new ConsumerStep.Of<>("String consumer step", str -> {
      //...
    });
    ConsumerStep<Integer> intConsumerStep = new ConsumerStep.Of<>("Integer consumer step", str -> {
      //...
    });
    BiConsumerStep<Integer, String> intStrBiConsumerStep = new BiConsumerStep.Of<>("Integer String consumer step", (integer, str) -> {
      //...
    });
    BiConsumerStep<String, Integer> strIntBiConsumerStep = new BiConsumerStep.Of<>("String Integer consumer step", (integer, str) -> {
      //...
    });

    stepContext()
      .exec(runnableStep)
      .exec(() -> step("Custom step", () -> {
        //...
      }))
      .with(strSupplierStep)
      .exec(strConsumerStep)
      .with(intSupplierStep)
      .exec(intConsumerStep)
      .exec(intStrBiConsumerStep)
      .exec((integer, str) -> step("Custom step", () -> {
        //...
      }))
      .exec(runnableStep)
      .previous()
      .exec(strConsumerStep)
      .it(chain -> step("Custom step", () -> chain
        .exec(strConsumerStep)
        .exec(str -> step("Inner custom step", () -> {
          //...
        }))
      ));

    stepContext(123, "value")
      .exec(intStrBiConsumerStep)
      .exec(intConsumerStep)
      .map((integer, str) -> str, (integer, str) -> integer)
      .exec(strIntBiConsumerStep);
  }
}
