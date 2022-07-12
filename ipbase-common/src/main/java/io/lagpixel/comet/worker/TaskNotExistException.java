package io.lagpixel.comet.worker;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskNotExistException extends RuntimeException {
    private final static Logger logger = LoggerFactory.getLogger(TaskNotExistException.class);

    public TaskNotExistException(int id){
        super("task [" + id + "] doesn't exist in worker registry");
        logger.error("task [" + id + "] doesn't exist in worker registry");
    }

    public TaskNotExistException(@NotNull Task<?> task, JobWorker worker){
        super("task with id: [" + task.getID() + "] doesn't exist in worker registry, probably job was done or cancelled and entry has been clear");
        logger.error("task with id: [" + task.getID() + "] doesn't exist in worker registry, probably job was done or cancelled and entry has been clear");
    }
}
