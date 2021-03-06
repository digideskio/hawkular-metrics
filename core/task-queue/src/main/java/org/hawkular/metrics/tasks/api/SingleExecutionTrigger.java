/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.tasks.api;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author jsanda
 */
public class SingleExecutionTrigger extends AbstractTrigger {

    private Long triggerTime;

    public static class Builder {

        private Long delay;
        private Long triggerTime;

        public Builder withDelay(long delay, TimeUnit timeUnit) {
            this.delay = TimeUnit.MILLISECONDS.convert(delay, timeUnit);
            return this;
        }

        public Builder withTriggerTime(long time) {
            this.triggerTime = time;
            return this;
        }

        public SingleExecutionTrigger build() {
            return new SingleExecutionTrigger(delay, triggerTime);
        }
    }

    private SingleExecutionTrigger(Long delay, Long triggerTime) {
        if (delay == null && triggerTime == null) {
            throw new IllegalArgumentException("Both [delay] and [triggerTime] cannot be null");
        }
        if (triggerTime != null) {
            this.triggerTime = getExecutionTime(triggerTime).getMillis();
        } else {
            this.triggerTime = getExecutionTime(now.get() + delay).getMillis();
        }
    }

    // TODO reduce visibility?
    // This is for internal use by TaskSchedulerImpl.
    public SingleExecutionTrigger(long triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Override
    public long getTriggerTime() {
        return triggerTime;
    }

    @Override
    public Trigger nextTrigger() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleExecutionTrigger that = (SingleExecutionTrigger) o;
        return Objects.equals(triggerTime, that.triggerTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerTime);
    }

    @Override
    public String toString() {
        return "SingleExecutionTrigger{" +
                "triggerTime=" + triggerTime +
                '}';
    }
}
