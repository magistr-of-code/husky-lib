package maxmag_change.husky.utill;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MathHelper {

    public static boolean inBound(float value, float min, float max){
        return min < value && value < max;
    }

    public static Box rotateBox(Box box, BlockRotation rotation){
        BlockPos min = StructureTemplate.transformAround(BlockPos.ofFloored(box.minX,box.minY,box.minZ), BlockMirror.NONE,rotation,BlockPos.ORIGIN);
        BlockPos max = StructureTemplate.transformAround(BlockPos.ofFloored(box.maxX,box.maxY,box.maxZ),BlockMirror.NONE,rotation,BlockPos.ORIGIN);

        box = new Box(min,max);

        return box;
    }

    public static Vec3d matrixToVec(MatrixStack matrixStack) {
        // Extract transformation matrix
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        // Convert local position to world space
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        return transformToWorld(matrix, camera);
    }

    // **Helper Method: Converts Rendered Item Position to World Space**
    private static Vec3d transformToWorld(Matrix4f matrix, Camera camera) {
        // Convert (0,0,0) in local item space to transformed coordinates
        Vector4f localPos = new Vector4f(0, 0, 0, 90);
        localPos = matrix.transform(localPos);

        // Convert view space to world space by adding the camera position
        Vec3d cameraPos = camera.getPos();
        return new Vec3d(cameraPos.x + localPos.x(), cameraPos.y + localPos.y(), cameraPos.z + localPos.z());
    }

    public static int clamp(long value, int min, int max) {
        return (int) Math.min(max, Math.max(value, min));
    }

    public static Box addMax(Box box, double value){
        return new Box(box.maxX + value ,box.maxY + value , box.maxZ + value, box.minX, box.minY, box.minZ);
    }

    public static Box addMaxY(Box box, double value){
        return new Box(box.maxX ,box.maxY + value , box.maxZ, box.minX, box.minY, box.minZ);
    }

    public static Box addMaxZ(Box box, double value){
        return new Box(box.maxX ,box.maxY , box.maxZ + value, box.minX, box.minY, box.minZ);
    }

    public static Box addMaxX(Box box, double value){
        return new Box(box.maxX + value ,box.maxY , box.maxZ, box.minX, box.minY, box.minZ);
    }

    public static Box addMin(Box box, double value){
        return new Box(box.maxX ,box.maxY , box.maxZ, box.minX + value, box.minY + value, box.minZ + value);
    }

    public static Box addMinX(Box box, double value){
        return new Box(box.maxX ,box.maxY , box.maxZ, box.minX + value, box.minY, box.minZ);
    }

    public static Box addMinY(Box box, double value){
        return new Box(box.maxX ,box.maxY , box.maxZ, box.minX, box.minY + value, box.minZ);
    }

    public static Box addMinZ(Box box, double value){
        return new Box(box.maxX ,box.maxY , box.maxZ, box.minX, box.minY, box.minZ + value);
    }

    public static Vec3d getMin(Box box){
        return new Vec3d(box.minX,box.minY,box.minZ);
    }

    public static Vec3d getMinYp1(Box box){
        return new Vec3d(box.minX,box.minY,box.minZ);
    }

    public static Vec3d getMinYp2(Box box){
        return new Vec3d(box.maxX,box.minY,box.minZ);
    }

    public static Vec3d getMinYp3(Box box){
        return new Vec3d(box.minX,box.minY,box.maxZ);
    }

    public static Vec3d getMinYp4(Box box){
        return new Vec3d(box.maxX,box.minY,box.maxZ);
    }

    public static double calculateLength(Box box){
        return new Vec3d(box.minX,box.minY,box.minZ).distanceTo(new Vec3d(box.maxX,box.maxY,box.maxZ));
    }
}
