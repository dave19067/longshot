package dc.longshot.game;

import dc.longshot.epf.EntityAdapted;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.GameSession;
import dc.longshot.models.GameSettings;
import dc.longshot.models.Level;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.AutoRotatePart;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.BoundsRemovePart;
import dc.longshot.parts.CityDamagePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.ColorChangePart;
import dc.longshot.parts.CurvedMovementPart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DamageOnSpawnPart;
import dc.longshot.parts.EmitPart;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.FragsPart;
import dc.longshot.parts.GhostPart;
import dc.longshot.parts.GravityPart;
import dc.longshot.parts.GroundShooterPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.PlaySoundOnSpawnPart;
import dc.longshot.parts.PointsPart;
import dc.longshot.parts.RotateToCursorPart;
import dc.longshot.parts.ShakeOnSpawnPart;
import dc.longshot.parts.SoundOnDeathPart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.SpawningPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.SpinPart;
import dc.longshot.parts.TargetShooterPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WanderMovementPart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.parts.converters.DrawablePartConverter.DrawablePart;
import dc.longshot.parts.converters.LightPartConverter.LightPart;
import dc.longshot.parts.converters.TransformPartConverter.TransformPart;

public final class XmlBindings {
	
	public static final Class<?>[] BOUND_CLASSES = {
		AlliancePart.class, 
		AttachmentPart.class, 
		AutoRotatePart.class, 
		BouncePart.class, 
		BoundsPart.class, 
		BoundsRemovePart.class, 
		CityDamagePart.class, 
		CollisionTypePart.class, 
		ColorChangePart.class, 
		CurvedMovementPart.class,
		DamageOnCollisionPart.class, 
		DamageOnSpawnPart.class, 
		DebugSettings.class, 
		DrawablePart.class, 
		EmitPart.class, 
		EntityAdapted.class,
		FollowerPart.class, 
		FragsPart.class, 
		GameSession.class, 
		GameSettings.class, 
		GhostPart.class, 
		GravityPart.class, 
		GroundShooterPart.class, 
		HealthPart.class, 
		Level.class, 
		LightPart.class, 
		PlaySoundOnSpawnPart.class, 
		PointsPart.class, 
		RotateToCursorPart.class, 
		ShakeOnSpawnPart.class, 
		SoundOnDeathPart.class, 
		SpawnOnDeathPart.class, 
		SpawningPart.class, 
		SpeedPart.class, 
		SpinPart.class, 
		TargetShooterPart.class, 
		TimedDeathPart.class, 
		TransformPart.class, 
		TranslatePart.class, 
		WanderMovementPart.class, 
		WaypointsPart.class, 
		WeaponPart.class
	};

}
