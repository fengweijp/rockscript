package io.rockscript.engine.impl;

import io.rockscript.engine.job.RetryPolicy;

import java.time.Instant;

public class ServiceFunctionErrorEvent extends ScriptExecutionErrorEvent<ArgumentsExpressionExecution> {

  Instant retryTime;

  /** constructor for gson deserialization
   * */
  ServiceFunctionErrorEvent() {
  }

  public ServiceFunctionErrorEvent(ArgumentsExpressionExecution execution, String error, Instant retryTime) {
    super(execution, error);
    this.retryTime = retryTime;
  }

  public Instant getRetryTime() {
    return retryTime;
  }

  @Override
  public void execute(ArgumentsExpressionExecution execution) {
    execution.incrementFailedAttemptsCount();
  }

  @Override
  public String toString() {
    return "[" + scriptExecutionId + "|" + executionId + "] " +
           "ServiceFunction error [script:"+scriptId+",line:"+line+"] "+error+(retryTime!=null ? ", retry scheduled for "+retryTime.toString() : "");
  }
}
