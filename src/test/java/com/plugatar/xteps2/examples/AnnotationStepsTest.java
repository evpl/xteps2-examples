package com.plugatar.xteps2.examples;

import com.plugatar.xteps2.annotation.DefaultStep;
import com.plugatar.xteps2.annotation.NotImplemented;
import com.plugatar.xteps2.annotation.Param;
import com.plugatar.xteps2.annotation.Step;
import com.plugatar.xteps2.core.StepNotImplementedError;
import org.junit.jupiter.api.Test;

import static com.plugatar.xteps2.Keywords.WHEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

final class AnnotationStepsTest {

  @Test
  void simpleSteps() {
    Object1 obj = new Object1();
    obj.action1();
    obj.action2();
    Object1.staticAction();
  }

  public static class Object1 {

    @Step("Create Object1")
    public Object1() {
      //...
    }

    @Step("Action1 step name")
    public void action1() {
      //...
    }

    @Step
    public void action2() {
      //...
    }

    @Step("Static step")
    public static void staticAction() {
      //...
    }
  }

  @Test
  void stepsWithParamsAndDescription() {
    Object2 obj = new Object2("secret value");
    obj.action1();
    obj.action2();
    Object2.staticAction();
  }

  public static class Object2 {

    @Step(name = "Create Object2", params = {@Param(name = "secret", masked = true), @Param(name = "param1", value = "value1")})
    public Object2(String secret) {
      //...
    }

    @Step(name = "Action1 step name", desc = "Step description")
    public void action1() {
      //...
    }

    @Step(keyword = WHEN, name = "User do smth")
    public void action2() {
      //...
    }

    @Step(value = "Static step")
    public static void staticAction() {
      //...
    }
  }

  @Test
  void stepsWithReplacements() {
    Object3 obj = new Object3("\"ctor value\"");
    obj.action();
  }

  public static class Object3 {
    public final String field = "fieldValue";

    @Step(name = "Create Object2 with value {param1} - the same value {0}")
    public Object3(String param1) {
      //...
    }

    @Step(name = "This: {this}, class: {class.getSimpleName()}", desc = "{this.toString()}, {this.field}")
    public void action() {
      //...
    }

    @Override
    public String toString() {
      return "toString() method result";
    }
  }

  @Test
  void defaultStepArtifacts() {
    Object4 obj = new Object4();
    obj.action();
    Object4.staticAction();
  }

  @DefaultStep(name = "{class.getSimpleName()} {method.getName()}", params = {@Param(name = "param1", value = "value")})
  public static class Object4 {

    @Step(ignoreDefault = true)
    public Object4() {
      //...
    }

    @Step
    public void action() {
      //...
    }

    @Step
    public static void staticAction() {
      //...
    }
  }

  @Test
  void notImplementedSteps() {
    assertThatThrownBy(() -> {
      new Object5();
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      new Object6().action();
    }).isInstanceOf(StepNotImplementedError.class);

    assertThatThrownBy(() -> {
      Object6.staticAction();
    }).isInstanceOf(StepNotImplementedError.class);
  }

  public static class Object5 {

    @Step
    @NotImplemented
    public Object5() {
      //...
    }
  }

  public static class Object6 {

    @Step
    public Object6() {
      //...
    }

    @Step
    @NotImplemented
    public void action() {
      //...
    }

    @Step
    @NotImplemented
    public static void staticAction() {
      //...
    }
  }
}
