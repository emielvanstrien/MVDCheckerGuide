package nl.tue.ddss.bcf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import org.xml.sax.SAXException;

import javax.xml.parsers.*;

public class TempGeometry {
	ArrayList<String> stringList = new ArrayList<String>();
	ArrayList<Float> floatList = new ArrayList<Float>();
	ArrayList<Float> xList = new ArrayList<Float>();
	ArrayList<Float> yList = new ArrayList<Float>();
	ArrayList<Float> zList = new ArrayList<Float>();
	private BoundingBox boundingBox;
	private BoundingBox globalBB;
	double cameraViewPointX;
	double cameraViewPointY;
	double cameraViewPointZ;
	double cameraUpVectorX;
	double cameraUpVectorY;
	double cameraUpVectorZ;
	double cameraDirectionX;
	double cameraDirectionY;
	double cameraDirectionZ;
	
	double gCameraViewPointX;
	double gCameraViewPointY;
	double gCameraViewPointZ;
	double gCameraUpVectorX;
	double gCameraUpVectorY;
	double gCameraUpVectorZ;
	double gCameraDirectionX;
	double gCameraDirectionY;
	double gCameraDirectionZ;
	
	
	public void setGlobalBB() throws ParserConfigurationException, SAXException{
		try {
			File file = new File("C:\\Temp\\TempGeometry.dae");
			Matrix4d matrix = new Matrix4d();
			matrix.setIdentity();
			globalBB = ColladaGlobalBB.getGlobalBB(file, matrix);

			Point3d max = globalBB.getMax();
			Point3d min = globalBB.getMin();

			double centerX = (max.getX() + min.getX()) / 2;
			double centerY = (max.getY() + min.getY()) / 2;
			double centerZ = (max.getZ() + min.getZ()) / 2;
			double lengthX = max.getX() - min.getX();
			double lengthY = max.getY() - min.getY();
			double lengthZ = max.getZ() - min.getZ();
			
			double lengthMax=lengthX;
			if (lengthY>lengthMax) lengthMax=lengthY;
			if (lengthZ>lengthMax) lengthMax=lengthZ;

			gCameraViewPointX = centerX
					+ ((2 * lengthMax) * Math.cos(Math.PI / 4));
			gCameraViewPointY = centerY
					+ ((2 * lengthMax) * Math.cos(Math.PI / 4));
			gCameraViewPointZ = centerZ + 0.5 * lengthMax;

			gCameraDirectionX = centerX - gCameraViewPointX;
			gCameraDirectionY = centerY - gCameraViewPointY;
			gCameraDirectionZ = centerZ - gCameraViewPointZ;

			gCameraUpVectorX = gCameraDirectionX;
			gCameraUpVectorY = gCameraDirectionY;
			gCameraUpVectorZ = ((Math.pow(gCameraDirectionX, 2) + Math.pow(
					gCameraDirectionY, 2)) / -gCameraDirectionZ);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void cleanUp(List<String> guids) throws SAXException,
			ParserConfigurationException {
		try {
			File file = new File("C:\\Temp\\TempGeometry.dae");
			Matrix4d matrix = new Matrix4d();
			matrix.setIdentity();
			boundingBox = ColladaBB.getBoundingBox(file, matrix, guids);

			Point3d max = boundingBox.getMax();
			Point3d min = boundingBox.getMin();

			double centerX = (max.getX() + min.getX()) / 2;
			double centerY = (max.getY() + min.getY()) / 2;
			double centerZ = (max.getZ() + min.getZ()) / 2;
			double lengthX = max.getX() - min.getX();
			double lengthY = max.getY() - min.getY();
			double lengthZ = max.getZ() - min.getZ();
			
			double lengthMax=lengthX;
			if (lengthY>lengthMax) lengthMax=lengthY;
			if (lengthZ>lengthMax) lengthMax=lengthZ;

			cameraViewPointX = centerX
					+ ((2 * lengthMax) * Math.cos(Math.PI / 4));
			cameraViewPointY = centerY
					+ ((2 * lengthMax) * Math.cos(Math.PI / 4));
			cameraViewPointZ = centerZ + 0.5 * lengthMax;

			cameraDirectionX = centerX - cameraViewPointX;
			cameraDirectionY = centerY - cameraViewPointY;
			cameraDirectionZ = centerZ - cameraViewPointZ;

			cameraUpVectorX = cameraDirectionX;
			cameraUpVectorY = cameraDirectionY;
			cameraUpVectorZ = ((Math.pow(cameraDirectionX, 2) + Math.pow(
					cameraDirectionY, 2)) / -cameraDirectionZ);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}