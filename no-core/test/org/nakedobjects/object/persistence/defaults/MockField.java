package org.nakedobjects.object.persistence.defaults;

import org.nakedobjects.object.DummyNakedObjectSpecification;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.reflect.NakedObjectField;


public class MockField extends NakedObjectField {

    private Naked contentObject;

    public MockField() {
        this(new DummyNakedObjectSpecification());
    }

    public MockField(NakedObjectSpecification type) {
        super("", type, null);
    }

    protected Naked get(NakedObject fromObject) {
        return contentObject;
    }

    protected String getLabel(NakedObject object) {
        return null;
    }

    public boolean hasHint() {
        return false;
    }

    public boolean isDerived() {
        return false;
    }

    public boolean isEmpty(NakedObject adapter) {
        return false;
    }

    public void setupFieldContent(Naked contentObject) {
        this.contentObject = contentObject;
    }

    public Object getExtension(Class cls) {
        return null;
    }

}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */