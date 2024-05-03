package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteMigration {
    // Instancia única de ByteMigration
    private static ByteMigration instance;

    // Constructor privado para evitar instanciación directa
    private ByteMigration() {
        // Constructor privado para evitar instanciación directa
    }

    // Método estático para obtener la única instancia de ByteMigration
    public static ByteMigration getInstance() {
        if (instance == null) {
            instance = new ByteMigration();
        }
        return instance;
    }

    /**
     *
     * @param bigendian
     * @return
     */
    public int bigToLittle_Int(int bigendian){
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(0);
    }

    /**
     *
     * @param bigendian
     * @return
     */
    public float bigToLittle_Float(float bigendian){
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putFloat(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(0);
    }

    /**
     *
     * @param bigendian
     * @return
     */
    public short bigToLittle_Short(short bigendian){
        ByteBuffer buf = ByteBuffer.allocate(2);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort(0);
    }
}