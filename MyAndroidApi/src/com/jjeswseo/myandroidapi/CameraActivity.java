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
	
	// 첫번째 후방 카메라 아이디
	int defaultCameraId;
	
	Camera mCamera;
	
	int cameraCurrentlyLocked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//윈도우 타이틀을 숨긴다.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//단말기의 풀화면을 사용한다.
		//상태바 등과 같은 화면 요소들은 숨겨진다.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//SurfaceView를 가지는 RelativeLayout컨테이너를 생성하고 Activity의 뷰로 할당해준다.
		//SurfaceView를 사용하기 위해서는 SurfaceHolder.Callback를 구현해야 한다.
		//SurfaceView에 사용자 Interaction이 일어나면 SurfaceHolder.Callback의 인터페이스가 호출되어 진다.
		
		mPreview = new PreView(this);
		
		setContentView(mPreview);
		
		//기기에 사용할 수 있는 카메라 갯수를 조회한다.
		numberOfCameras = Camera.getNumberOfCameras();
		
		CameraInfo cameraInfo = new CameraInfo();
		
		for(int i=0 ; i<numberOfCameras; i++){
			Camera.getCameraInfo(i, cameraInfo);
			
			//현재 카메라의 위치가 후방카메라라면 default카메라로 셋팅
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
 * CustomLayout을 사용하는 클래스
 * 
 * Layout.onMeasure(int, int) : 자식뷰와 자기자신의 사이즈를 결정하기 위해 호출된다.
 * Layout.onLayout(boolean, int, int, int, int) : 자식View들의 Size와 Position을 결정하여 배치해야 할 때 호출된다.
 * Layout.onSizeChanged(int, int, int, int) : 자신의 Size가 변경되면 호출된다. 
 * 
 * 
 * abstract void  surfaceChanged(SurfaceHolder holder, int format, int width, int height)
 * SurfaceView에 구조적인 변화(format이나 size)가 일어날때 호출된다. 최초 한번은 SurfaceView가 생성될 시점에(surfaceCreated()) 이어서 호출된다.
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
		
		//SurfaceView를 생성하여 ViewGroup(Container)에 add한다. 
		mSurfaceView = new SurfaceView(context);
		addView(mSurfaceView);
		
		//SurfaceView와 인터페이스 할 Holder를 할당해 준다. 
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		
	}
	
	public void setCamera(Camera camera){
		mCamera = camera;
		if(mCamera != null){
			mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
			
			//레이아웃을 새로 적용시켜야 할 때 호출한다.
			requestLayout();
		}
	}
	
	//Layout Override 시작
	
	
	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 * widthMeasureSpec : 부모클래스에서 기본적으로 산정한 width
	 * heightMeasureSpec : 부모클래스에서 기본적으로 산정한 height
	 * 
	 * onMeasure함수를 Override한다면 반드시 setMeasuredDimension()을 통해
	 * 산정된 값을 셋팅해 주어야 한다.
	 * 셋팅 안될시는 런타임Exception (MeasureTime Exception)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		
		//Layout(SurfaceView)의 최적의 크기를 산정한다.
		//getSuggestedMinimumWidth() 뷰가 가지는 최소의 너비
		//getSuggestedMinimumHeight() 뷰가 가지는 최소의 높이
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		
		//산정된 값을 셋팅해준다.
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
	
	
	
	// SurfaceView Callback Override 시작
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//SurfaceView가 생성될때 호출되는 Callback
		
		try{
			if(mCamera != null){
				//Camera에 Preview를 생성된 SurfaceView에 그리도록 셋팅한다.
				mCamera.setPreviewDisplay(holder);
			}
		} catch(IOException exception){
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		//SurfaceView에 구조적인 변화(format이나 size)가 일어날때 호출된다. 
		//최초 한번은 SurfaceView가 생성될 시점에(surfaceCreated()) 이어서 호출된다.
		
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