/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package com.microsoft.java.test.runner;

import com.microsoft.java.test.runner.listeners.CustomizedJUnitTestListener;

import com.microsoft.java.test.runner.listeners.JUnitExecutionListener;
import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class CustomizedJUnitCoreRunner extends JUnitCore {
    public void run(String[] suites) {
        List<JUnit4TestReference> testSuites = TestRunnerUtil.createTestReferences(suites);
        if (testSuites.isEmpty()) {
            TestingMessageHelper.reporterAttached(TestOutputSream.instance());
            return;
        }

        CustomizedJUnitTestListener delegate = new CustomizedJUnitTestListener();
        RunListener listener = new JUnitExecutionListener(delegate);
        RunNotifier runNotifier = new RunNotifier();
        runNotifier.addListener(listener);
        delegate.testRunStarted();
        for (JUnit4TestReference testReference: testSuites) {
            testReference.sendTree(delegate);
        }

        Result result = new Result();
        final RunListener resultListener = result.createListener();
        runNotifier.addListener(resultListener);

        for (JUnit4TestReference testReference : testSuites) {
            testReference.run(runNotifier);
        }
        runNotifier.fireTestRunFinished(result);
    }
}

