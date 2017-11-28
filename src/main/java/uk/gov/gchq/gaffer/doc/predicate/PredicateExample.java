/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.gchq.gaffer.doc.predicate;

import org.apache.commons.lang.StringUtils;

import uk.gov.gchq.gaffer.doc.util.Example;
import uk.gov.gchq.koryphe.signature.Signature;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.util.function.Predicate;

public abstract class PredicateExample extends Example {
    public PredicateExample(final Class<? extends Predicate> classForExample) {
        super(classForExample);
    }

    public void runExample(final Predicate predicate, final String description, final Object... inputs) {
        print("### " + getMethodNameAsSentence(1) + "\n");
        if (StringUtils.isNotBlank(description)) {
            print(description + "\n");
        }

        printJavaJsonPython(predicate, 3);

        print("Input type:");
        print("\n```");
        final StringBuilder inputClasses = new StringBuilder();
        for (final Class<?> item : Signature.getInputSignature(predicate).getClasses()) {
            inputClasses.append(item.getName());
            inputClasses.append(", ");
        }
        print(inputClasses.substring(0, inputClasses.length() - 2));
        print("```\n");

        print("Example inputs:");
        print("<table>");
        print("<tr><th>Type</th><th>Input</th><th>Result</th></tr>");
        for (final Object input : inputs) {
            final String inputType;
            final String inputString;
            if (!(input instanceof Tuple) || input instanceof MapTuple) {
                if (null == input) {
                    inputType = "";
                    inputString = "null";
                } else {
                    inputType = input.getClass().getName();
                    inputString = String.valueOf(input);
                }
            } else {
                final StringBuilder inputTypeBuilder = new StringBuilder("[");
                final StringBuilder inputStringBuilder = new StringBuilder("[");
                for (final Object item : (Tuple) input) {
                    if (null == item) {
                        inputTypeBuilder.append(" ,");
                        inputStringBuilder.append("null, ");
                    } else {
                        inputTypeBuilder.append(item.getClass().getName());
                        inputTypeBuilder.append(", ");

                        inputStringBuilder.append(String.valueOf(item));
                        inputStringBuilder.append(", ");
                    }
                }
                inputType = inputTypeBuilder.substring(0, inputTypeBuilder.length() - 2) + "]";
                inputString = inputStringBuilder.substring(0, inputStringBuilder.length() - 2) + "]";
            }

            String result;
            try {
                result = String.valueOf(predicate.test(input));
            } catch (final Exception e) {
                result = e.toString();
            }

            print("<tr><td>" + inputType + "</td><td>" + inputString + "</td><td>" + result + "</td></tr>");
        }
        print("</table>\n");
        print(METHOD_DIVIDER);
    }
}
