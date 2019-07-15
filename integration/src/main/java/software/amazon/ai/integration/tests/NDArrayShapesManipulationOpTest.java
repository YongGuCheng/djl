/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package software.amazon.ai.integration.tests;

import java.util.Arrays;
import software.amazon.ai.integration.exceptions.FailedTestException;
import software.amazon.ai.integration.util.AbstractTest;
import software.amazon.ai.integration.util.Assertions;
import software.amazon.ai.integration.util.RunAsTest;
import software.amazon.ai.ndarray.NDArray;
import software.amazon.ai.ndarray.NDArrays;
import software.amazon.ai.ndarray.NDList;
import software.amazon.ai.ndarray.NDManager;
import software.amazon.ai.ndarray.types.Shape;

public class NDArrayShapesManipulationOpTest extends AbstractTest {
    NDManager manager = NDManager.newBaseManager();

    public static void main(String[] args) {
        new NDArrayShapesManipulationOpTest().runTest(args);
    }

    @RunAsTest
    public void testSplit() throws FailedTestException {
        NDArray original = manager.create(new Shape(2, 2), new float[] {1f, 2f, 3f, 4f});
        NDList splitted = original.split();
        Assertions.assertEquals(
                splitted.head(), manager.create(new Shape(2), new float[] {1f, 2f}));
        Assertions.assertEquals(
                splitted.get(1), manager.create(new Shape(2), new float[] {3f, 4f}));
    }

    @RunAsTest
    public void testFlatten() throws FailedTestException {
        NDArray original = manager.create(new Shape(2, 2), new float[] {1f, 2f, 3f, 4f});
        NDArray flattened = original.flatten();
        NDArray expected = manager.create(new Shape(4), new float[] {1f, 2f, 3f, 4f});
        Assertions.assertEquals(flattened, expected);
    }

    @RunAsTest
    public void testReshape() throws FailedTestException {
        NDArray original = manager.create(new Shape(3, 2), new float[] {1f, 2f, 3f, 4f, 5f, 6f});
        NDArray reshaped = original.reshape(new Shape(2, 3));
        NDArray expected = manager.create(new Shape(2, 3), new float[] {1f, 2f, 3f, 4f, 5f, 6f});
        Assertions.assertEquals(reshaped, expected);
        reshaped = original.reshape(new Shape(2, -1));
        Assertions.assertEquals(reshaped, expected);
    }

    @RunAsTest
    public void testExpandDim() throws FailedTestException {
        NDArray original = manager.create(new Shape(2), new int[] {1, 2});
        Assertions.assertTrue(
                Arrays.equals(
                        original.expandDims(0).getShape().getShape(), new Shape(1, 2).getShape()));
    }

    @RunAsTest
    public void testStack() throws FailedTestException {
        NDArray original = manager.create(new Shape(2), new float[] {1f, 2f});
        NDArray expect = manager.create(new Shape(2, 2), new float[] {1f, 2f, 1f, 2f});
        Assertions.assertEquals(original.stack(original), expect);
    }

    @RunAsTest
    public void testConcat() throws FailedTestException {
        NDArray concatedND = manager.create(new Shape(1), new float[] {1f});
        NDArray concatedND2 = manager.create(new Shape(1), new float[] {2f});
        NDArray actual = manager.create(new Shape(2), new float[] {1f, 2f});

        Assertions.assertEquals(concatedND.concat(new NDArray[] {concatedND2}, 0), actual);
        Assertions.assertEquals(
                NDArrays.concat(new NDArray[] {concatedND, concatedND2}, 0), actual);
        Assertions.assertEquals(concatedND.concat(concatedND2), actual);
    }
}
