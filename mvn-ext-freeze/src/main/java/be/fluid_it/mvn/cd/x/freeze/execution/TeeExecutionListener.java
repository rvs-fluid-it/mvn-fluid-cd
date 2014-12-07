package be.fluid_it.mvn.cd.x.freeze.execution;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionListener;

public class TeeExecutionListener implements ExecutionListener {
    private final ExecutionListener firstExecutionListener;
    private final ExecutionListener secondExecutionListener;

    public TeeExecutionListener(ExecutionListener firstExecutionListener, ExecutionListener secondExecutionListener) {
        this.firstExecutionListener = firstExecutionListener;
        this.secondExecutionListener = secondExecutionListener;
    }


    @Override
    public void projectDiscoveryStarted(ExecutionEvent event) {
        firstExecutionListener.projectDiscoveryStarted(event);
        secondExecutionListener.projectDiscoveryStarted(event);
    }

    @Override
    public void sessionStarted(ExecutionEvent event) {
        firstExecutionListener.sessionStarted(event);
        secondExecutionListener.sessionStarted(event);
    }

    @Override
    public void sessionEnded(ExecutionEvent event) {
        firstExecutionListener.sessionEnded(event);
        secondExecutionListener.sessionEnded(event);
    }

    @Override
    public void projectSkipped(ExecutionEvent event) {
        firstExecutionListener.projectSkipped(event);
        secondExecutionListener.projectSkipped(event);
    }

    @Override
    public void projectStarted(ExecutionEvent event) {
        firstExecutionListener.projectStarted(event);
        secondExecutionListener.projectStarted(event);
    }

    @Override
    public void projectSucceeded(ExecutionEvent event) {
        firstExecutionListener.projectSucceeded(event);
        secondExecutionListener.projectSucceeded(event);
    }

    @Override
    public void projectFailed(ExecutionEvent event) {
        firstExecutionListener.projectFailed(event);
        secondExecutionListener.projectFailed(event);
    }

    @Override
    public void mojoSkipped(ExecutionEvent event) {
        firstExecutionListener.mojoSkipped(event);
        secondExecutionListener.mojoSkipped(event);
    }

    @Override
    public void mojoStarted(ExecutionEvent event) {
        firstExecutionListener.mojoStarted(event);
        secondExecutionListener.mojoStarted(event);
    }

    @Override
    public void mojoSucceeded(ExecutionEvent event) {
        firstExecutionListener.mojoSucceeded(event);
        secondExecutionListener.mojoSucceeded(event);
    }

    @Override
    public void mojoFailed(ExecutionEvent event) {
        firstExecutionListener.mojoFailed(event);
        secondExecutionListener.mojoFailed(event);
    }

    @Override
    public void forkStarted(ExecutionEvent event) {
        firstExecutionListener.forkStarted(event);
        secondExecutionListener.forkStarted(event);
    }

    @Override
    public void forkSucceeded(ExecutionEvent event) {
        firstExecutionListener.forkSucceeded(event);
        secondExecutionListener.forkSucceeded(event);
    }

    @Override
    public void forkFailed(ExecutionEvent event) {
        firstExecutionListener.forkFailed(event);
        secondExecutionListener.forkFailed(event);
    }

    @Override
    public void forkedProjectStarted(ExecutionEvent event) {
        firstExecutionListener.forkedProjectStarted(event);
        secondExecutionListener.forkedProjectStarted(event);
    }

    @Override
    public void forkedProjectSucceeded(ExecutionEvent event) {
        firstExecutionListener.forkedProjectSucceeded(event);
        secondExecutionListener.forkedProjectSucceeded(event);
    }

    @Override
    public void forkedProjectFailed(ExecutionEvent event) {
        firstExecutionListener.forkedProjectFailed(event);
        secondExecutionListener.forkedProjectFailed(event);
    }
}
