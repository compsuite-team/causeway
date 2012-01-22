/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.core.commons.config;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.isis.core.commons.debug.DebugBuilder;
import org.apache.isis.core.commons.exceptions.IsisException;
import org.apache.isis.core.commons.resource.ResourceStreamSource;
import org.apache.log4j.Logger;

public class IsisConfigurationDefault implements IsisConfiguration {
    private static final Logger LOG = Logger.getLogger(IsisConfigurationDefault.class);
    private final Properties properties = new Properties();
    private final ResourceStreamSource resourceStreamSource;

    // ////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////

    public IsisConfigurationDefault() {
        this(null);
    }

    public IsisConfigurationDefault(final ResourceStreamSource resourceStreamSource) {
        this.resourceStreamSource = resourceStreamSource;
        LOG.debug("from :" + nameOf(resourceStreamSource));
    }

    private String nameOf(final ResourceStreamSource resourceStreamSource) {
        return resourceStreamSource != null ? resourceStreamSource.getName() : null;
    }

    // ////////////////////////////////////////////////
    // ResourceStreamSource
    // ////////////////////////////////////////////////

    @Override
    public ResourceStreamSource getResourceStreamSource() {
        return resourceStreamSource;
    }

    // ////////////////////////////////////////////////
    // add
    // ////////////////////////////////////////////////

    /**
     * Add the properties from an existing Properties object.
     */
    public void add(final Properties properties) {
        final Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            final String name = (String) e.nextElement();
            this.properties.put(name, properties.getProperty(name));
        }
    }

    /**
     * Adds a key-value pair to this set of properties
     */
    public void add(final String key, final String value) {
        if (key == null) {
            return;
        }
        if (properties.containsKey(key)) {
            LOG.info("replacing " + key + "=" + properties.get(key) + " with " + value);
        }
        properties.put(key, value);
    }

    @Override
    public IsisConfiguration createSubset(final String prefix) {
        final IsisConfigurationDefault subset = new IsisConfigurationDefault(resourceStreamSource);

        String startsWith = prefix;
        if (!startsWith.endsWith(".")) {
            startsWith = startsWith + '.';
        }
        final int prefixLength = startsWith.length();

        final Enumeration<?> e = properties.keys();
        while (e.hasMoreElements()) {
            final String key = (String) e.nextElement();
            if (key.startsWith(startsWith)) {
                final String modifiedKey = key.substring(prefixLength);
                subset.properties.put(modifiedKey, properties.get(key));
            }
        }
        return subset;
    }

    // ////////////////////////////////////////////////
    // getXxx
    // ////////////////////////////////////////////////

    /**
     * Gets the boolean value for the specified name where no value or 'on' will
     * result in true being returned; anything gives false. If no boolean
     * property is specified with this name then false is returned.
     * 
     * @param name
     *            the property name
     */
    @Override
    public boolean getBoolean(final String name) {
        return getBoolean(name, false);
    }

    /**
     * Gets the boolean value for the specified name. If no property is
     * specified with this name then the specified default boolean value is
     * returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the value to use as a default
     */
    @Override
    public boolean getBoolean(final String name, final boolean defaultValue) {
        String value = getProperty(name);
        if (value == null) {
            return defaultValue;
        }
        value = value.toLowerCase();
        if (value.equals("on") || value.equals("yes") || value.equals("true") || value.equals("")) {
            return true;
        }
        if (value.equals("off") || value.equals("no") || value.equals("false")) {
            return false;
        }

        throw new IsisConfigurationException("Illegal flag for " + name + "; must be one of on, off, yes, no, true or false");
    }

    /**
     * Gets the color for the specified name. If no color property is specified
     * with this name then null is returned.
     * 
     * @param name
     *            the property name
     */
    @Override
    public Color getColor(final String name) {
        return getColor(name, null);
    }

    /**
     * Gets the color for the specified name. If no color property is specified
     * with this name then the specified default color is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the value to use as a default
     */
    @Override
    public Color getColor(final String name, final Color defaultValue) {
        final String color = getProperty(name);

        if (color == null) {
            return defaultValue;
        }

        return Color.decode(color);
    }

    @Override
    public void debugData(final DebugBuilder str) {
        str.appendln("Resource Stream Source", resourceStreamSource);
        str.appendln();
        final Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            str.append(name, 55);
            str.append(" = ");
            str.appendln(properties.getProperty(name));
        }
    }

    @Override
    public String debugTitle() {
        return "Properties Configuration";
    }

    /**
     * Gets the font for the specified name. If no font property is specified
     * with this name then null is returned.
     * 
     * @param name
     *            the property name
     */
    @Override
    public Font getFont(final String name) {
        return getFont(name, null);
    }

    /**
     * Gets the font for the specified name. If no font property is specified
     * with this name then the specified default font is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the color to use as a default
     */
    @Override
    public Font getFont(final String name, final Font defaultValue) {
        final String font = getProperty(name);

        if (font == null) {
            return defaultValue;
        }

        return Font.decode(font);
    }

    /**
     * Gets the number value for the specified name. If no property is specified
     * with this name then 0 is returned.
     * 
     * @param name
     *            the property name
     */
    @Override
    public int getInteger(final String name) {
        return getInteger(name, 0);
    }

    /**
     * Gets the number value for the specified name. If no property is specified
     * with this name then the specified default number value is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the value to use as a default
     */
    @Override
    public int getInteger(final String name, final int defaultValue) {
        final String value = getProperty(name);

        if (value == null) {
            return defaultValue;
        }

        return Integer.valueOf(value).intValue();
    }

    @Override
    public String[] getList(final String name) {
        final String list = getString(name);
        if (list == null) {
            return new String[0];
        } else {
            final StringTokenizer tokens = new StringTokenizer(list, ConfigurationConstants.LIST_SEPARATOR);
            final String array[] = new String[tokens.countTokens()];
            int i = 0;
            while (tokens.hasMoreTokens()) {
                array[i++] = tokens.nextToken().trim();
            }
            return array;
        }
    }

    @Override
    public IsisConfiguration getProperties(final String withPrefix) {
        final int prefixLength = "".length();

        final Properties pp = new Properties();
        final Enumeration<?> e = properties.keys();
        while (e.hasMoreElements()) {
            final String key = (String) e.nextElement();
            if (key.startsWith(withPrefix)) {
                final String modifiedKey = key.substring(prefixLength);
                pp.put(modifiedKey, properties.get(key));
            }
        }
        final IsisConfigurationDefault isisConfigurationDefault = new IsisConfigurationDefault(resourceStreamSource);
        isisConfigurationDefault.add(pp);
        return isisConfigurationDefault;
    }

    private String getProperty(final String name) {
        return getProperty(name, null);
    }

    private String getProperty(final String name, final String defaultValue) {
        final String key = referedToAs(name);
        if (key.indexOf("..") >= 0) {
            throw new IsisException("property names should not have '..' within them: " + name);
        }
        String property = properties.getProperty(key, defaultValue);
        property = property != null ? property.trim() : null;
        LOG.debug("property: '" + key + "' =  '" + property + "'");
        return property;
    }

    /**
     * Returns the configuration property with the specified name. If there is
     * no matching property then null is returned.
     */
    @Override
    public String getString(final String name) {
        return getProperty(name);
    }

    @Override
    public String getString(final String name, final String defaultValue) {
        return getProperty(name, defaultValue);
    }

    @Override
    public boolean hasProperty(final String name) {
        final String key = referedToAs(name);
        return properties.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public Iterator<String> iterator() {
        return properties.stringPropertyNames().iterator();
    }

    /**
     * Returns as a String that the named property is refered to as. For example
     * in a simple properties file the property z might be specified in the file
     * as x.y.z.
     */
    private String referedToAs(final String name) {
        return name;
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public String toString() {
        return "ConfigurationParameters [properties=" + properties + "]";
    }

    // ////////////////////////////////////////////////////////////////////
    // injectInto
    // ////////////////////////////////////////////////////////////////////

    @Override
    public void injectInto(final Object candidate) {
        if (IsisConfigurationAware.class.isAssignableFrom(candidate.getClass())) {
            final IsisConfigurationAware cast = IsisConfigurationAware.class.cast(candidate);
            cast.setIsisConfiguration(this);
        }
    }

}
