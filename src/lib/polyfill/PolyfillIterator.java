package lib.polyfill;

import java.util.Iterator;

public abstract class PolyfillIterator<T> implements Iterator<T> {
    /* BEGIN-POLYFILL-6 *../
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    /..* END-POLYFILL-6 */
}
