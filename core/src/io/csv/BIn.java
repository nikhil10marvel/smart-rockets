package io.csv;

import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class BIn {

    Serializable obj;
    final boolean compress;

    private BIn(Serializable obj, boolean compress){
        this.obj = obj;
        this.compress = compress;
    }

    private BIn(boolean compress){
        this.compress = compress;
    }

    public static class ValueBox<T,R> implements Map.Entry<T, R> {

        T val1;
        R val2;
        public ValueBox(T val1, R val2){
            this.val1 = val1;
            this.val2 = val2;
        }

        @Override
        public T getKey() {
            return val1;
        }

        @Override
        public R getValue() {
            return val2;
        }

        @Override
        public R setValue(R r) {
            this.val2 = r;
            return val2;
        }
    }

    public static BIn getSerializer(FileHandle file, Serializable obj, boolean compr){
        BIn bin = new BIn(obj, compr);
        try {
            OutputStream stream = file.write(false);
            bin.serialize(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bin;
    }

    public static ValueBox<BIn, Serializable> getSerializer(FileHandle file, boolean compr){
        BIn bin = new BIn(compr);
        Serializable obj = null;
        try {
            InputStream inputStream = file.read();
            obj = bin.deserialize(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ValueBox<>(bin, obj);
    }

    private void serialize(OutputStream outputStream){
        try {
            ObjectOutputStream objectOutputStream;
            GZIPOutputStream gzipOutputStream = null;
            if(compress){
                gzipOutputStream = new GZIPOutputStream(outputStream);
                objectOutputStream = new ObjectOutputStream(gzipOutputStream);
            } else {
                objectOutputStream = new ObjectOutputStream(outputStream);
            }
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            if(compress) {
                assert gzipOutputStream != null;
                gzipOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Serializable deserialize(InputStream inputStream){
        Serializable obj = null;
        try {
            GZIPInputStream gzipInputStream = null;
            ObjectInputStream objectInputStream;
            if(compress){
                gzipInputStream = new GZIPInputStream(inputStream);
                objectInputStream = new ObjectInputStream(gzipInputStream);
            } else {
                objectInputStream = new ObjectInputStream(inputStream);
            }
            obj = (Serializable) objectInputStream.readObject();
            objectInputStream.close();
            if(compress){
                assert gzipInputStream != null;
                gzipInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public byte[] serializeAsBytes(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        serialize(byteArrayOutputStream);
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public Serializable deserializeFromBytes(byte[] data){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Serializable obj = deserialize(byteArrayInputStream);
        try {
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
