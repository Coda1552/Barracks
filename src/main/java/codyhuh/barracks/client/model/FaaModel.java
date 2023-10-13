package codyhuh.barracks.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class FaaModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart body;
	private final ModelPart tailBase;

	public FaaModel(ModelPart root) {
		this.body = root.getChild("body");
		this.tailBase = body.getChild("tailBase");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -2.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(10, 11).addBox(-1.5F, 0.5F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -1.0F));

		PartDefinition horns = body.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -6.0F, -3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0F));

		PartDefinition tailBase = body.addOrReplaceChild("tailBase", CubeListBuilder.create().texOffs(8, 21).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 2.5F));

		PartDefinition tail = tailBase.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.25F, 0.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, 0.5F, -0.1745F, 0.0F, 0.0F));

		PartDefinition caudalFin = tail.addOrReplaceChild("caudalFin", CubeListBuilder.create().texOffs(1, 13).addBox(0.0F, -2.0F, -0.5F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 5.5F));

		PartDefinition analFin = body.addOrReplaceChild("analFin", CubeListBuilder.create().texOffs(8, 25).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 2.5F, -0.2618F, 0.0F, 0.0F));

		PartDefinition rightFin = body.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(14, 21).mirror().addBox(-1.75F, -1.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 1.0F, -1.0F, -0.4645F, 0.1709F, -0.4829F));

		PartDefinition leftFin = body.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(14, 21).addBox(-0.25F, -1.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.0F, -1.0F, -0.4645F, -0.1709F, 0.4829F));

		PartDefinition leftEye = body.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(0, 25).addBox(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3F, -1.5F, -2.2F, -0.0233F, -1.0054F, -0.0255F));

		PartDefinition rightEye = body.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(-0.25F, -0.75F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.3F, -1.5F, -2.2F, -0.0233F, 1.0054F, 0.0255F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.xRot = headPitch * ((float)Math.PI / 180F);
		this.body.yRot = netHeadYaw * ((float)Math.PI / 180F);

		float f = 1.0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}

		//this.tailBase.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}