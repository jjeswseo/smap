package com.jjeswseo.myandroidapi;

import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author swseo
 * 
 * 
 */
public class CameraActivity extends Activity {
	private PreView mPreview = null;
	int numberOfCameras;
	
	// ù��° �Ĺ� ī�޶� ���̵�
	int defaultCameraId;
	
	Camera mCamera;
	
	int cameraCurrentlyLocked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//������ Ÿ��Ʋ�� �����.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//�ܸ����� Ǯȭ���� ����Ѵ�.
		//���¹� ��� ���� ȭ�� ��ҵ��� ��������.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//SurfaceView�� ������ RelativeLayout�����̳ʸ� �����ϰ� Activity�� ��� �Ҵ����ش�.
		//SurfaceView�� ����ϱ� ���ؼ��� SurfaceHolder.Callback�� �����ؾ� �Ѵ�.
		//SurfaceView�� ����� Interaction�� �Ͼ�� SurfaceHolder.Callback�� �������̽��� ȣ��Ǿ� ����.
		
		mPreview = new PreView(this);
		
		setContentView(mPreview);
		
		//��⿡ ����� �� �ִ� ī�޶� ������ ��ȸ�Ѵ�.
		numberOfCameras = Camera.getNumberOfCameras();
		
		CameraInfo cameraInfo = new CameraInfo();
		
		for(int i=0 ; i<numberOfCameras; i++){
			Camera.getCameraInfo(i, cameraInfo);
			
			//���� ī�޶��� ��ġ�� �Ĺ�ī�޶��� defaultī�޶�� ����
			if(cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK){
				defaultCameraId = i;
			}
		}
		
	}
	@Override
	protected void onResume(){
		super.onResume();
		
		mCamera = Camera.open();
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		if(mCamera != null){
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}

/**
 * @author swseo
 * 
 * CustomLayout�� ����ϴ� Ŭ����
 * 
 * Layout.onMeasure(int, int) : �ڽĺ�� �ڱ��ڽ��� ����� �����ϱ� ���� ȣ��ȴ�.
 * Layout.onLayout(boolean, int, int, int, int) : �ڽ�View���� Size�� Position�� �����Ͽ� ��ġ�ؾ� �� �� ȣ��ȴ�.
 * Layout.onSizeChanged(int, int, int, int) : �ڽ��� Size�� ����Ǹ� ȣ��ȴ�. 
 * 
 * 
 * abstract void  surfaceChanged(SurfaceHolder holder, int format, int width, int height)
 * SurfaceView�� �������� ��ȭ(format�̳� size)�� �Ͼ�� ȣ��ȴ�. ���� �ѹ��� SurfaceView�� ������ ������(surfaceCreated()) �̾ ȣ��ȴ�.
 * 
 * abstract void  surfaceCreated(SurfaceHolder holder)  
 * 
 * abstract void  surfaceDestroyed(SurfaceHolder holder) 
 *
 */
class PreView extends ViewGroup implements SurfaceHolder.Callback{
	
	private final String TAG = "Preview";
	
	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	Camera mCamera;
	
	public PreView(Context context) {
		super(context);
		
		//SurfaceView�� �����Ͽ� ViewGroup(Container)�� add�Ѵ�. 
		mSurfaceView = new SurfaceView(context);
		addView(mSurfaceView);
		
		//SurfaceView�� �������̽� �� Holder�� �Ҵ��� �ش�. 
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		
	}
	
	public void setCamera(Camera camera){
		mCamera = camera;
		if(mCamera != null){
			mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
			
			//���̾ƿ��� ���� ������Ѿ� �� �� ȣ���Ѵ�.
			requestLayout();
		}
	}
	
	//Layout Override ����
	
	
	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 * widthMeasureSpec : �θ�Ŭ�������� �⺻������ ������ width
	 * heightMeasureSpec : �θ�Ŭ�������� �⺻������ ������ height
	 * 
	 * onMeasure�Լ��� Override�Ѵٸ� �ݵ�� setMeasuredDimension()�� ����
	 * ������ ���� ������ �־�� �Ѵ�.
	 * ���� �ȵɽô� ��Ÿ��Exception (MeasureTime Exception)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		
		//Layout(SurfaceView)�� ������ ũ�⸦ �����Ѵ�.
		//getSuggestedMinimumWidth() �䰡 ������ �ּ��� �ʺ�
		//getSuggestedMinimumHeight() �䰡 ������ �ּ��� ����
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		
		//������ ���� �������ش�.
		setMeasuredDimension(width, height);
		
		//TODO
		if(mSupportedPreviewSizes != null){
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
		}
		
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed && getChildCount() > 0){
			final View child = getChildAt(0);
			
			final int width = r - l;
			final int height = b - t;
			
			int previewWidth = width;
			int previewHeight = height;
			
			if(mPreviewSize != null){
				previewWidth = mPreviewSize.width;
				previewHeight = mPreviewSize.height;
			}
			
			//TODO
			if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
		}
	}
	
	//TODO
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
	
	
	
	// SurfaceView Callback Override ����
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//SurfaceView�� �����ɶ� ȣ��Ǵ� Callback
		
		try{
			if(mCamera != null){
				//Camera�� Preview�� ������ SurfaceView�� �׸����� �����Ѵ�.
				mCamera.setPreviewDisplay(holder);
			}
		} catch(IOException exception){
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		//SurfaceView�� �������� ��ȭ(format�̳� size)�� �Ͼ�� ȣ��ȴ�. 
		//���� �ѹ��� SurfaceView�� ������ ������(surfaceCreated()) �̾ ȣ��ȴ�.
		
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		requestLayout();
		
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mCamera != null){
			mCamera.stopPreview();
		}
	}
}