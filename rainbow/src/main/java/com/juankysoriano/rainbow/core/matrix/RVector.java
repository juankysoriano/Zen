/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2008 Dan Shiffman
  Copyright (c) 2008-10 Ben Fry and Casey Reas

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License version 2.1 as published by the Free Software Foundation.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package com.juankysoriano.rainbow.core.matrix;

import com.juankysoriano.rainbow.core.Rainbow;
import com.juankysoriano.rainbow.utils.RainbowMath;

import java.io.Serializable;

/**
 * ( begin auto-generated from PVector.xml )
 * <p/>
 * A class to describe a two or three dimensional vector. This datatype
 * stores two or three variables that are commonly used as a position,
 * velocity, and/or acceleration. Technically, <em>position</em> is a point
 * and <em>velocity</em> and <em>acceleration</em> are vectors, but this is
 * often simplified to consider all three as vectors. For example, if you
 * consider a rectangle moving across the screen, at any given instant it
 * has a position (the object's location, expressed as a point.), a
 * velocity (the rate at which the object's position changes per time unit,
 * expressed as a vector), and acceleration (the rate at which the object's
 * velocity changes per time unit, expressed as a vector). Since vectors
 * represent groupings of values, we cannot simply use traditional
 * addition/multiplication/etc. Instead, we'll need to do some "vector"
 * math, which is made easy by the methods inside the <b>PVector</b>
 * class.<br />
 * <br />
 * The methods for this class are extensive. For a complete list, visit the
 * <a
 * href="http://processing.googlecode.com/svn/trunk/processing/build/javadoc/core/">developer's reference.</a>
 * <p/>
 * ( end auto-generated )
 * <p/>
 * A class to describe a two or three dimensional vector.
 * <p/>
 * The result of all functions are applied to the vector itself, with the
 * exception of cross(), which returns a new PVector (or writes to a specified
 * 'target' PVector). That is, add() will add the contents of one vector to
 * this one. Using add() with additional parameters allows you to put the
 * result into a new PVector. Functions that act on multiple vectors also
 * include static versions. Because creating new objects can be computationally
 * expensive, most functions include an optional 'target' PVector, so that a
 * new PVector object is not created with each operation.
 * <p/>
 * Initially based on the Vector3D class by <a href="http://www.shiffman.net">Dan Shiffman</a>.
 *
 * @webref math
 */
public final class RVector implements Serializable {

    /**
     * Generated 2010-09-14 by jdf
     */
    private static final long serialVersionUID = -6717872085945400694L;

    /**
     * ( begin auto-generated from PVector_x.xml )
     * <p/>
     * The x component of the vector. This field (variable) can be used to both
     * get and set the value (see above example.)
     * <p/>
     * ( end auto-generated )
     *
     * @webref pvector:field
     * @usage web_application
     * @brief The x component of the vector
     */
    public float x;

    /**
     * ( begin auto-generated from PVector_y.xml )
     * <p/>
     * The y component of the vector. This field (variable) can be used to both
     * get and set the value (see above example.)
     * <p/>
     * ( end auto-generated )
     *
     * @webref pvector:field
     * @usage web_application
     * @brief The y component of the vector
     */
    public float y;

    /**
     * ( begin auto-generated from PVector_z.xml )
     * <p/>
     * The z component of the vector. This field (variable) can be used to both
     * get and set the value (see above example.)
     * <p/>
     * ( end auto-generated )
     *
     * @webref pvector:field
     * @usage web_application
     * @brief The z component of the vector
     */
    public float z;

    /**
     * Array so that this can be temporarily used in an array context
     */
    transient protected float[] array;

    /**
     * Constructor for an empty vector: x, y, and z are set to 0.
     */
    public RVector() {
    }

    /**
     * Constructor for a 3D vector.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public RVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor for a 2D vector: z coordinate is set to 0.
     */
    public RVector(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * ( begin auto-generated from PVector_random2D.xml )
     * <p/>
     * Make a new 2D unit vector with a random direction.  If you pass in "this"
     * as an argument, it will use the Imagine's random number generator.  You can
     * also pass in a target PVector to fill.
     *
     * @return the random PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Make a new 2D unit vector with a random direction.
     */
    public static RVector random2D() {
        return random2D(null, null);
    }

    /**
     * Make a new 2D unit vector with a random direction
     * using Processing's current random number generator
     *
     * @param parent current Imagine instance
     * @return the random PVector
     */
    public static RVector random2D(Rainbow parent) {
        return random2D(null, parent);
    }

    /**
     * Set a 2D vector to a random unit vector with a random direction
     *
     * @param target the target vector (if null, a new vector will be created)
     * @return the random PVector
     */
    public static RVector random2D(RVector target) {
        return random2D(target, null);
    }

    /**
     * Make a new 2D unit vector with a random direction
     *
     * @param parent current Imagine instance
     * @param target the target vector (if null, a new vector will be created)
     * @return the random PVector
     */
    public static RVector random2D(RVector target, Rainbow parent) {
        if (parent == null) {
            return fromAngle((float) (Math.random() * Math.PI * 2), target);
        } else {
            return fromAngle(RainbowMath.random(RainbowMath.TWO_PI), target);
        }
    }

    /**
     * ( begin auto-generated from PVector_random3D.xml )
     * <p/>
     * Make a new 3D unit vector with a random direction.  If you pass in "this"
     * as an argument, it will use the Imagine's random number generator.  You can
     * also pass in a target PVector to fill.
     *
     * @return the random PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Make a new 3D unit vector with a random direction.
     */
    public static RVector random3D() {
        return random3D(null, null);
    }

    /**
     * Make a new 3D unit vector with a random direction
     * using Processing's current random number generator
     *
     * @param parent current Imagine instance
     * @return the random PVector
     */
    public static RVector random3D(Rainbow parent) {
        return random3D(null, parent);
    }

    /**
     * Set a 3D vector to a random unit vector with a random direction
     *
     * @param target the target vector (if null, a new vector will be created)
     * @return the random PVector
     */
    public static RVector random3D(RVector target) {
        return random3D(target, null);
    }

    /**
     * Make a new 3D unit vector with a random direction
     *
     * @param target the target vector (if null, a new vector will be created)
     * @param parent current Imagine instance
     * @return the random PVector
     */
    public static RVector random3D(RVector target, Rainbow parent) {
        float angle;
        float vz;
        if (parent == null) {
            angle = (float) (Math.random() * Math.PI * 2);
            vz = (float) (Math.random() * 2 - 1);
        } else {
            angle = RainbowMath.random(RainbowMath.TWO_PI);
            vz = RainbowMath.random(-1, 1);
        }
        float vx = (float) (Math.sqrt(1 - vz * vz) * Math.cos(angle));
        float vy = (float) (Math.sqrt(1 - vz * vz) * Math.sin(angle));
        if (target == null) {
            target = new RVector(vx, vy, vz);
            //target.normalize(); // Should be unnecessary
        } else {
            target.set(vx, vy, vz);
        }
        return target;
    }

    /**
     * ( begin auto-generated from PVector_sub.xml )
     * <p/>
     * Make a new 2D unit vector from an angle.
     * <p/>
     * ( end auto-generated )
     *
     * @param angle the angle
     * @return the new unit PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Make a new 2D unit vector from an angle
     */
    public static RVector fromAngle(float angle) {
        return fromAngle(angle, null);
    }

    /**
     * Make a new 2D unit vector from an angle
     *
     * @param angle  the angle
     * @param target the target vector (if null, a new vector will be created)
     * @return the PVector
     */
    public static RVector fromAngle(float angle, RVector target) {
        if (target == null) {
            target = new RVector((float) Math.cos(angle), (float) Math.sin(angle), 0);
        } else {
            target.set((float) Math.cos(angle), (float) Math.sin(angle), 0);
        }
        return target;
    }

    /**
     * Add two vectors
     *
     * @param v1 a vector
     * @param v2 another vector
     */
    public static RVector add(RVector v1, RVector v2) {
        return add(v1, v2, null);
    }

    /**
     * Add two vectors into a target vector
     *
     * @param target the target vector (if null, a new vector will be created)
     */
    public static RVector add(RVector v1, RVector v2, RVector target) {
        if (target == null) {
            target = new RVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }
        return target;
    }

    /**
     * Subtract one vector from another
     *
     * @param v1 the x, y, and z components of a PVector object
     * @param v2 the x, y, and z components of a PVector object
     */
    public static RVector sub(RVector v1, RVector v2) {
        return sub(v1, v2, null);
    }

    /**
     * Subtract one vector from another and store in another vector
     *
     * @param v1     the x, y, and z components of a PVector object
     * @param v2     the x, y, and z components of a PVector object
     * @param target PVector to store the result
     */
    public static RVector sub(RVector v1, RVector v2, RVector target) {
        if (target == null) {
            target = new RVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }

    /**
     * @param v the vector to multiply by the scalar
     */
    public static RVector mult(RVector v, float n) {
        return mult(v, n, null);
    }

    /**
     * Multiply a vector by a scalar, and write the result into a target PVector.
     *
     * @param target PVector to store the result
     */
    public static RVector mult(RVector v, float n, RVector target) {
        if (target == null) {
            target = new RVector(v.x * n, v.y * n, v.z * n);
        } else {
            target.set(v.x * n, v.y * n, v.z * n);
        }
        return target;
    }

    /**
     * @param v1 the x, y, and z components of a PVector
     * @param v2 the x, y, and z components of a PVector
     */
    public static RVector mult(RVector v1, RVector v2) {
        return mult(v1, v2, null);
    }

    public static RVector mult(RVector v1, RVector v2, RVector target) {
        if (target == null) {
            target = new RVector(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
        } else {
            target.set(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
        }
        return target;
    }

    /**
     * Divide a vector by a scalar and return the result in a new vector.
     *
     * @param v any variable of type PVector
     * @param n the number to divide with the vector
     * @return a new vector that is v1 / n
     */
    public static RVector div(RVector v, float n) {
        return div(v, n, null);
    }

    /**
     * Divide a vector by a scalar and store the result in another vector.
     *
     * @param v      any variable of type PVector
     * @param n      the number to divide with the vector
     * @param target PVector to store the result
     */
    public static RVector div(RVector v, float n, RVector target) {
        if (target == null) {
            target = new RVector(v.x / n, v.y / n, v.z / n);
        } else {
            target.set(v.x / n, v.y / n, v.z / n);
        }
        return target;
    }

    /**
     * Divide each element of one vector by the individual elements of another
     * vector, and return the result as a new PVector.
     */
    public static RVector div(RVector v1, RVector v2) {
        return div(v1, v2, null);
    }

    public static RVector div(RVector v1, RVector v2, RVector target) {
        if (target == null) {
            target = new RVector(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
        } else {
            target.set(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
        }
        return target;
    }

    /**
     * @param v1 any variable of type PVector
     * @param v2 any variable of type PVector
     * @return the Euclidean distance between v1 and v2
     */
    public static float dist(RVector v1, RVector v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * @param v1 any variable of type PVector
     * @param v2 any variable of type PVector
     */
    public static float dot(RVector v1, RVector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    /**
     * @param v1     any variable of type PVector
     * @param v2     any variable of type PVector
     * @param target PVector to store the result
     */
    public static RVector cross(RVector v1, RVector v2, RVector target) {
        float crossX = v1.y * v2.z - v2.y * v1.z;
        float crossY = v1.z * v2.x - v2.z * v1.x;
        float crossZ = v1.x * v2.y - v2.x * v1.y;

        if (target == null) {
            target = new RVector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }

    /**
     * Linear interpolate between two vectors (returns a new PVector object)
     *
     * @param v1  the vector
     * @param v2  the vector to lerp to
     * @param amt The amt parameter is the amount to interpolate between the two vectors where 1.0 equal to the new vector
     *            0.1 is very near the new vector, 0.5 is half-way in between.
     * @return the resulting lerped PVector
     */
    public static RVector lerp(RVector v1, RVector v2, float amt) {
        RVector v = v1.get();
        v.lerp(v2, amt);
        return v;
    }

    /**
     * ( begin auto-generated from PVector_angleBetween.xml )
     * <p/>
     * Calculates and returns the angle (in radians) between two vectors.
     * <p/>
     * ( end auto-generated )
     *
     * @param v1 the x, y, and z components of a PVector
     * @param v2 the x, y, and z components of a PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate and return the angle between two vectors
     */
    public static float angleBetween(RVector v1, RVector v2) {
        double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        // This should be a number between -1 and 1, since it's "normalized"
        double amt = dot / (v1mag * v2mag);
        // But if it's not due to rounding error, then we need to fix it
        // http://code.google.com/p/processing/issues/detail?id=340
        // Otherwise if outside the range, acos() will return NaN
        // http://www.cppreference.com/wiki/c/math/acos
        if (amt <= -1) {
            return RainbowMath.PI;
        } else if (amt >= 1) {
            // http://code.google.com/p/processing/issues/detail?id=435
            return 0;
        }
        return (float) Math.acos(amt);
    }

    /**
     * ( begin auto-generated from PVector_set.xml )
     * <p/>
     * Sets the x, y, and z component of the vector using three separate
     * variables, the data from a PVector, or the values from a float array.
     * <p/>
     * ( end auto-generated )
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     * @webref pvector:method
     * @brief Set the x, y, and z component of the vector
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @param v any variable of type PVector
     */
    public void set(RVector v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    /**
     * Set the x, y (and maybe z) coordinates using a float[] array as the source.
     *
     * @param source array to copy from
     */
    public void set(float[] source) {
        if (source.length >= 2) {
            x = source[0];
            y = source[1];
        }
        if (source.length >= 3) {
            z = source[2];
        }
    }

    /**
     * ( begin auto-generated from PVector_get.xml )
     * <p/>
     * Gets a copy of the vector, returns a PVector object.
     * <p/>
     * ( end auto-generated )
     *
     * @webref pvector:method
     * @usage web_application
     * @brief Get a copy of the vector
     */
    public RVector get() {
        return new RVector(x, y, z);
    }

    /**
     * @param target
     */
    public float[] get(float[] target) {
        if (target == null) {
            return new float[]{x, y, z};
        }
        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        if (target.length >= 3) {
            target[2] = z;
        }
        return target;
    }

    /**
     * ( begin auto-generated from PVector_mag.xml )
     * <p/>
     * Calculates the magnitude (length) of the vector and returns the result
     * as a float (this is simply the equation <em>sqrt(x*x + y*y + z*z)</em>.)
     * <p/>
     * ( end auto-generated )
     *
     * @return magnitude (length) of the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the magnitude of the vector
     */
    public float mag() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * ( begin auto-generated from PVector_mag.xml )
     * <p/>
     * Calculates the squared magnitude of the vector and returns the result
     * as a float (this is simply the equation <em>(x*x + y*y + z*z)</em>.)
     * Faster if the real length is not required in the
     * case of comparing vectors, etc.
     * <p/>
     * ( end auto-generated )
     *
     * @return squared magnitude of the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the magnitude of the vector
     */
    public float magSq() {
        return (x * x + y * y + z * z);
    }

    /**
     * ( begin auto-generated from PVector_add.xml )
     * <p/>
     * Adds x, y, and z components to a vector, adds one vector to another, or
     * adds two independent vectors together. The version of the method that
     * adds two vectors together is a static method and returns a PVector, the
     * others have no return value -- they act directly on the vector. See the
     * examples for more context.
     * <p/>
     * ( end auto-generated )
     *
     * @param v the vector to be added
     * @webref pvector:method
     * @usage web_application
     * @brief Adds x, y, and z components to a vector, one vector to another, or two independent vectors
     */
    public void add(RVector v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    /**
     * @param x x component of the vector
     * @param y y component of the vector
     * @param z z component of the vector
     */
    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * ( begin auto-generated from PVector_sub.xml )
     * <p/>
     * Subtracts x, y, and z components from a vector, subtracts one vector
     * from another, or subtracts two independent vectors. The version of the
     * method that subtracts two vectors is a static method and returns a
     * PVector, the others have no return value -- they act directly on the
     * vector. See the examples for more context.
     * <p/>
     * ( end auto-generated )
     *
     * @param v any variable of type PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Subtract x, y, and z components from a vector, one vector from another, or two independent vectors
     */
    public void sub(RVector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    /**
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     */
    public void sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    /**
     * ( begin auto-generated from PVector_mult.xml )
     * <p/>
     * Multiplies a vector by a scalar or multiplies one vector by another.
     * <p/>
     * ( end auto-generated )
     *
     * @param n the number to multiply with the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Multiply a vector by a scalar or one vector by another
     */
    public void mult(float n) {
        x *= n;
        y *= n;
        z *= n;
    }

    public void mult(RVector v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
    }

    /**
     * ( begin auto-generated from PVector_div.xml )
     * <p/>
     * Divides a vector by a scalar or divides one vector by another.
     * <p/>
     * ( end auto-generated )
     *
     * @param n the value to divide by
     * @webref pvector:method
     * @usage web_application
     * @brief Divide a vector by a scalar or one vector by another
     */
    public void div(float n) {
        x /= n;
        y /= n;
        z /= n;
    }

    /**
     * Divide each element of one vector by the elements of another vector.
     */
    public void div(RVector v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
    }

    /**
     * ( begin auto-generated from PVector_dist.xml )
     * <p/>
     * Calculates the Euclidean distance between two points (considering a
     * point as a vector object).
     * <p/>
     * ( end auto-generated )
     *
     * @param v the x, y, and z coordinates of a PVector
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the distance between two points
     */
    public float dist(RVector v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * ( begin auto-generated from PVector_dot.xml )
     * <p/>
     * Calculates the dot product of two vectors.
     * <p/>
     * ( end auto-generated )
     *
     * @param v any variable of type PVector
     * @return the dot product
     * @webref pvector:method
     * @usage web_application
     * @brief Calculate the dot product of two vectors
     */
    public float dot(RVector v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * @param x x component of the vector
     * @param y y component of the vector
     * @param z z component of the vector
     */
    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    /**
     * ( begin auto-generated from PVector_cross.xml )
     * <p/>
     * Calculates and returns a vector composed of the cross product between
     * two vectors.
     * <p/>
     * ( end auto-generated )
     *
     * @param v the vector to calculate the cross product
     * @webref pvector:method
     * @brief Calculate and return the cross product
     */
    public RVector cross(RVector v) {
        return cross(v, null);
    }

    /**
     * @param v      any variable of type PVector
     * @param target PVector to store the result
     */
    public RVector cross(RVector v, RVector target) {
        float crossX = y * v.z - v.y * z;
        float crossY = z * v.x - v.z * x;
        float crossZ = x * v.y - v.x * y;

        if (target == null) {
            target = new RVector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }

    /**
     * ( begin auto-generated from PVector_normalize.xml )
     * <p/>
     * Normalize the vector to length 1 (make it a unit vector).
     * <p/>
     * ( end auto-generated )
     *
     * @webref pvector:method
     * @usage web_application
     * @brief Normalize the vector to a length of 1
     */
    public void normalize() {
        float m = mag();
        if (m != 0 && m != 1) {
            div(m);
        }
    }

    /**
     * @param target Set to null to create a new vector
     * @return a new vector (if target was null), or target
     */
    public RVector normalize(RVector target) {
        if (target == null) {
            target = new RVector();
        }
        float m = mag();
        if (m > 0) {
            target.set(x / m, y / m, z / m);
        } else {
            target.set(x, y, z);
        }
        return target;
    }

    /**
     * ( begin auto-generated from PVector_limit.xml )
     * <p/>
     * Limit the magnitude of this vector to the value used for the <b>max</b> parameter.
     * <p/>
     * ( end auto-generated )
     *
     * @param max the maximum magnitude for the vector
     * @webref pvector:method
     * @usage web_application
     * @brief Limit the magnitude of the vector
     */
    public void limit(float max) {
        if (magSq() > max * max) {
            normalize();
            mult(max);
        }
    }

    /**
     * ( begin auto-generated from PVector_setMag.xml )
     * <p/>
     * Set the magnitude of this vector to the value used for the <b>len</b> parameter.
     * <p/>
     * ( end auto-generated )
     *
     * @param len the new length for this vector
     * @webref pvector:method
     * @usage web_application
     * @brief Set the magnitude of the vector
     */
    public void setMag(float len) {
        normalize();
        mult(len);
    }

    /**
     * Sets the magnitude of this vector, storing the result in another vector.
     *
     * @param target Set to null to create a new vector
     * @param len    the new length for the new vector
     * @return a new vector (if target was null), or target
     */
    public RVector setMag(RVector target, float len) {
        target = normalize(target);
        target.mult(len);
        return target;
    }

    /**
     * ( begin auto-generated from PVector_setMag.xml )
     * <p/>
     * Calculate the angle of rotation for this vector (only 2D vectors)
     * <p/>
     * ( end auto-generated )
     *
     * @return the angle of rotation
     * @webref pvector:method
     * @usage web_application
     * @brief SCalculate the angle of rotation for this vector
     */
    public float heading() {
        float angle = (float) Math.atan2(-y, x);
        return -1 * angle;
    }

    @Deprecated
    public float heading2D() {
        return heading();
    }

    /**
     * ( begin auto-generated from PVector_rotate.xml )
     * <p/>
     * Rotate the vector by an angle (only 2D vectors), magnitude remains the same
     * <p/>
     * ( end auto-generated )
     *
     * @param theta the angle of rotation
     * @webref pvector:method
     * @usage web_application
     * @brief Rotate the vector by an angle (2D only)
     */
    public void rotate(float theta) {
        float xTemp = x;
        // Might need to check for rounding errors like with angleBetween function?
        x = x * RainbowMath.cos(theta) - y * RainbowMath.sin(theta);
        y = xTemp * RainbowMath.sin(theta) + y * RainbowMath.cos(theta);
    }

    /**
     * ( begin auto-generated from PVector_rotate.xml )
     * <p/>
     * Linear interpolate the vector to another vector
     * <p/>
     * ( end auto-generated )
     *
     * @param v   the vector to lerp to
     * @param amt The amt parameter is the amount to interpolate between the two vectors where 1.0 equal to the new vector
     *            0.1 is very near the new vector, 0.5 is half-way in between.
     * @webref pvector:method
     * @usage web_application
     * @brief Linear interpolate the vector to another vector
     */
    public void lerp(RVector v, float amt) {
        x = RainbowMath.lerp(x, v.x, amt);
        y = RainbowMath.lerp(y, v.y, amt);
        z = RainbowMath.lerp(z, v.z, amt);
    }

    /**
     * Linear interpolate the vector to x,y,z values
     *
     * @param x   the x component to lerp to
     * @param y   the y component to lerp to
     * @param z   the z component to lerp to
     * @param amt The amt parameter is the amount to interpolate between the two vectors where 1.0 equal to the new vector
     *            0.1 is very near the new vector, 0.5 is half-way in between.
     */
    public void lerp(float x, float y, float z, float amt) {
        this.x = RainbowMath.lerp(this.x, x, amt);
        this.y = RainbowMath.lerp(this.y, y, amt);
        this.z = RainbowMath.lerp(this.z, z, amt);
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @Override
    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

    /**
     * ( begin auto-generated from PVector_array.xml )
     * <p/>
     * Return a representation of this vector as a float array. This is only
     * for temporary use. If used in any other fashion, the contents should be
     * copied by using the <b>PVector.get()</b> method to copy into your own array.
     * <p/>
     * ( end auto-generated )
     *
     * @webref pvector:method
     * @usage: web_application
     * @brief Return a representation of the vector as a float array
     */
    public float[] array() {
        if (array == null) {
            array = new float[3];
        }
        array[0] = x;
        array[1] = y;
        array[2] = z;
        return array;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RVector)) {
            return false;
        }
        final RVector p = (RVector) obj;
        return x == p.x && y == p.y && z == p.z;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }
}
