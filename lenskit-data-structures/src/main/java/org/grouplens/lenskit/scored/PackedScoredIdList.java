package org.grouplens.lenskit.scored;

import com.google.common.base.Preconditions;
import org.grouplens.lenskit.collections.FastCollection;
import org.grouplens.lenskit.symbols.Symbol;
import org.grouplens.lenskit.symbols.TypedSymbol;

import java.util.AbstractList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

/**
 * A space-efficient, unboxed list of scored IDs.  These lists are immutable; create them using a
 * {@link ScoredIdListBuilder}.
 *
 * @since 1.4
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public final class PackedScoredIdList extends AbstractList<ScoredId> implements FastCollection<ScoredId> {
    private final long[] ids;
    private final double[] scores;
    private final Map<Symbol,PackedChannel> channels;
    private final Map<TypedSymbol<?>,PackedTypedChannel> typedChannels;

    PackedScoredIdList(long[] ids, double[] scores,
                       Map<Symbol, PackedChannel> chans,
                       Map<TypedSymbol<?>, PackedTypedChannel> tchans) {
        this.ids = ids;
        this.scores = scores;
        channels = chans;
        typedChannels = tchans;
    }

    @Override
    public int size() {
        return ids.length;
    }

    @Override
    public Iterator<ScoredId> fastIterator() {
        return iterator();
    }

    @Override
    public ScoredId get(int i) {
        Preconditions.checkElementIndex(i, size());
        ScoredIdBuilder bld = new ScoredIdBuilder(ids[i], scores[i]);
        for (PackedChannel chan: channels.values()) {
            if (chan.hasValue(i)) {
                bld.addChannel(chan.symbol, chan.get(i));
            }
        }
        for (PackedTypedChannel chan: typedChannels.values()) {
            if (chan.hasValue(i)) {
                bld.addChannel(chan.symbol, chan.get(i));
            }
        }
        return bld.build();
    }

    abstract static class PackedChannel {
        private final Symbol symbol;

        PackedChannel(Symbol sym) {
            symbol = sym;
        }

        abstract boolean hasValue(int idx);
        abstract double get(int idx);
    }

    static class FullPackedChannel extends PackedChannel {
        private final double[] values;
        private final BitSet used;

        FullPackedChannel(Symbol sym, double[] data, BitSet mask) {
            super(sym);
            used = mask;
            values = data;
        }

        @Override
        boolean hasValue(int idx) {
            return used == null || used.get(idx);
        }

        @Override
        double get(int idx) {
            return values[idx];
        }
    }

    abstract static class PackedTypedChannel {
        @SuppressWarnings("rawtypes")
        private final TypedSymbol symbol;

        private PackedTypedChannel(TypedSymbol<?> sym) {
            symbol = sym;
        }

        protected abstract boolean hasValue(int idx);
        protected abstract Object get(int idx);
    }

    static class FullPackedTypedChannel extends PackedTypedChannel {
        private final Object[] values;
        private final BitSet used;

        FullPackedTypedChannel(TypedSymbol<?> sym, Object[] data, BitSet mask) {
            super(sym);
            used = mask;
            values = data;
        }

        @Override
        protected boolean hasValue(int idx) {
            return used == null || used.get(idx);
        }

        @Override
        protected Object get(int idx) {
            return values[idx];
        }
    }
}