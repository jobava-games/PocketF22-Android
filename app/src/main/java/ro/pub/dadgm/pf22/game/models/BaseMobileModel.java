package ro.pub.dadgm.pf22.game.models;

import ro.pub.dadgm.pf22.physics.CollisionObject;
import ro.pub.dadgm.pf22.physics.Force;
import ro.pub.dadgm.pf22.physics.MobileObject;
import ro.pub.dadgm.pf22.utils.Point3D;
import ro.pub.dadgm.pf22.utils.Vector3D;

/**
 * The common model class for mobile objects.
 * 
 * <p>Usable with the physics engine to automatically update the position / speed of the object and 
 * check for collision detection.</p>
 */
public abstract class BaseMobileModel extends BaseModel implements MobileObject, CollisionObject {
	
	/**
	 * The position of the object.
	 */
	protected Point3D position;
	
	/**
	 * The velocity of the object.
	 * 
	 * <p>Initially, the object is static - v = (0, 0, 0).</p>
	 */
	protected Vector3D velocity;
	
	
	/**
	 * Default constructor. Initializes the position / vector objects.
	 */
	protected BaseMobileModel() {
		position = new Point3D();
		velocity = new Vector3D();
	}
	
	
	/**
	 * By default, no forces act on an object.
	 *
	 * @return Empty collection.
	 */
	@Override
	public Force[] getForces() {
		return new Force[0];
	}
	
	@Override
	public Point3D getPosition() {
		return position;
	}
	
	@Override
	public Vector3D getVelocity() {
		return velocity;
	}
	
}
