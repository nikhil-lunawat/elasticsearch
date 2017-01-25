/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.persistent;

import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry.Entry;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.AbstractWireSerializingTestCase;
import org.elasticsearch.persistent.TestPersistentActionPlugin.TestPersistentAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersistentTasksInProgressTests extends AbstractWireSerializingTestCase<PersistentTasksInProgress> {

    @Override
    protected PersistentTasksInProgress createTestInstance() {
        int numberOfTasks = randomInt(10);
        List<PersistentTasksInProgress.PersistentTaskInProgress<?>> entries = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            entries.add(new PersistentTasksInProgress.PersistentTaskInProgress<>(
                    randomLong(), randomAsciiOfLength(10), new TestPersistentActionPlugin.TestRequest(randomAsciiOfLength(10)),
                    randomAsciiOfLength(10)));
        }
        return new PersistentTasksInProgress(randomLong(), entries);
    }

    @Override
    protected Writeable.Reader<PersistentTasksInProgress> instanceReader() {
        return PersistentTasksInProgress::new;
    }

    @Override
    protected NamedWriteableRegistry getNamedWriteableRegistry() {
        return new NamedWriteableRegistry(Collections.singletonList(
                new Entry(PersistentActionRequest.class, TestPersistentAction.NAME, TestPersistentActionPlugin.TestRequest::new)
        ));
    }
}
