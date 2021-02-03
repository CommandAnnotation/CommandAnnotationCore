package skywolf46.commandannotation.data.command;

import skywolf46.commandannotation.abstraction.AbstractParseDefine;
import skywolf46.commandannotation.data.parser.ArgumentParser;
import skywolf46.commandannotation.util.ParameterStorage;

import java.util.Iterator;

public class CommandArgument implements Iterable<String> {
    private String cmd;
    private String[] args;
    private ParameterStorage storage;
    private int pointer = 0;
    private long key;

    public CommandArgument(ParameterStorage storage, String command, String[] args, long key) {
        this.storage = storage;
        this.args = args;
        this.key = key;
        this.cmd = command;
    }

    public ParameterStorage getStorage() {
        return storage;
    }

    public ArgumentParser convert(Object... params) {
        return ArgumentParser.parse(this, params);
    }


    public ArgumentParser convert(Class<?>... params) {
        return ArgumentParser.parse(this, (Object[]) params);
    }


    public ArgumentParser convert(AbstractParseDefine<?>... params) {
        return ArgumentParser.parse(this, (Object[]) params);
    }

    public void nextPointer() {
        pointer++;
    }

    public int length() {
        return args.length - pointer;
    }

    public int lengthOriginal() {
        return args.length;
    }

    public int pointer() {
        return pointer;
    }

    public String getArgumentBefore() {
        return getArgumentBefore(false);
    }

    public String getArgumentBefore(boolean addSpaceAfter) {
        return getOriginalOrNull(0, pointer) + (addSpaceAfter ? " " : "");
    }


    public String getCommand() {
        return cmd;
    }

    public String getOriginal(int start) {
        if (start < 0)
            throw new IndexOutOfBoundsException("Argument index < 0");
        int len = args.length;
        if (len <= start) {
            throw new IndexOutOfBoundsException("Argument overflow");
        }
        return args[start];
    }


    public String getOriginalOrNull(int start) {
        if (start < 0)
            return null;
        int len = args.length;
        if (len <= start) {
            return null;
        }
        return args[start];
    }

    public String getOriginalOrNull(int start, int end) {
        if (start < 0)
            return null;
        int len = args.length;
        if (len < end) {
            return null;
        }
        if (start == end)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(args[start + i]).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    public String getOriginal(int start, int end) {
        if (start < 0)
            throw new IndexOutOfBoundsException("Argument index < 0");
        int len = args.length;
        if (len < end) {
            throw new IndexOutOfBoundsException("Argument overflow");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(args[start + i]).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String get(int start) {
        if (start < 0)
            throw new IndexOutOfBoundsException("Argument index < 0");
        int len = length();
        if (len <= start) {
            throw new IndexOutOfBoundsException("Argument overflow");
        }
        return args[start + pointer];
    }

    public String getOrNull(int start) {
        if (start < 0)
            return null;
        int len = length();
        if (len <= start) {
            return null;
        }
        return args[start + pointer];
    }

    public String get(int start, int end) {
        if (start < 0)
            throw new IndexOutOfBoundsException("Argument index < 0");
        int len = length();
        if (len < end) {
            throw new IndexOutOfBoundsException("Argument overflow");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(args[start + pointer + i]).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public CommandIterator iterator() {
        return new CommandIterator();
    }

    public class CommandIterator implements Iterator<String> {
        private int iteratePointer = pointer;

        @Override
        public boolean hasNext() {
            return iteratePointer < args.length;
        }

        @Override
        public String next() {
            return args[iteratePointer++];
        }

        public String peekPrevious() {
            return args[iteratePointer - 1];
        }

        public String peek() {
            return args[iteratePointer];
        }

        public int left() {
            return args.length - (iteratePointer + 1);
        }
    }
}
