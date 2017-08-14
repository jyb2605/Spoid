/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spoid;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spoid.env.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraConnectionFragment extends Fragment {

  static ArrayList<Item> items_list;
  static ResultAdapter resultAdapter;

  Context mContext;
  static ArrayList<Item> hlist;
  static SectionListDataAdapter itemAdapter;
  RecyclerView recyclerView;
  RecyclerView.Adapter Adapter;
  RecyclerView.LayoutManager layoutManager;

  private DialogDefActivity def_dialog;

  private static final Logger LOGGER = new Logger();

  /**
   * The camera preview size will be chosen to be the smallest frame by pixel size capable of
   * containing a DESIRED_SIZE x DESIRED_SIZE square.
   */
  private static final int MINIMUM_PREVIEW_SIZE = 320;

  private RecognitionScoreView scoreView;
  ListView scoreListView;







  Button indicator;
  Button sum;
  Button quest;
  LinearLayout item_list;
  int item_list_width;
  boolean isListOpen=false;
  ObjectAnimator animator;
  static LinearLayout parse_layout;







  /**
   * Conversion from screen rotation to JPEG orientation.
   */
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
  private static final String FRAGMENT_DIALOG = "dialog";

  static {
    ORIENTATIONS.append(Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }

  /**
   * {@link android.view.TextureView.SurfaceTextureListener} handles several lifecycle events on a
   * {@link TextureView}.
   */
  private final TextureView.SurfaceTextureListener surfaceTextureListener =
      new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSurfaceTextureAvailable(
                final SurfaceTexture texture, final int width, final int height) {
          openCamera(width, height);

//          DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
//          int mwidth = dm.widthPixels;
//          int mheight = dm.heightPixels;
//          openCamera(mwidth, mheight);

          Log.e("SurfaceTexture size" , width + " x " + height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(
                final SurfaceTexture texture, final int width, final int height) {
          configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
          return true;
        }

        @Override
        public void onSurfaceTextureUpdated(final SurfaceTexture texture) {}
      };

  /**
   * ID of the current {@link CameraDevice}.
   */
  private String cameraId;

  /**
   * An {@link AutoFitTextureView} for camera preview.
   */
  private AutoFitTextureView textureView;

  /**
   * A {@link CameraCaptureSession } for camera preview.
   */
  private CameraCaptureSession captureSession;

  /**
   * A reference to the opened {@link CameraDevice}.
   */
  private CameraDevice cameraDevice;

  /**
   * The rotation in degrees of the camera sensor from the display.
   */
  private Integer sensorOrientation;

  /**
   * The {@link android.util.Size} of camera preview.
   */
  private Size previewSize;

  /**
   * {@link android.hardware.camera2.CameraDevice.StateCallback}
   * is called when {@link CameraDevice} changes its state.
   */
  private final CameraDevice.StateCallback stateCallback =
      new CameraDevice.StateCallback() {
        @Override
        public void onOpened(final CameraDevice cd) {
          // This method is called when the camera is opened.  We start camera preview here.
          cameraOpenCloseLock.release();
          cameraDevice = cd;
          createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(final CameraDevice cd) {
          cameraOpenCloseLock.release();
          cd.close();
          cameraDevice = null;
        }

        @Override
        public void onError(final CameraDevice cd, final int error) {
          cameraOpenCloseLock.release();
          cd.close();
          cameraDevice = null;
          final Activity activity = getActivity();
          if (null != activity) {
            activity.finish();
          }
        }
      };

  /**
   * An additional thread for running tasks that shouldn't block the UI.
   */
  private HandlerThread backgroundThread;

  /**
   * A {@link Handler} for running tasks in the background.
   */
  private Handler backgroundHandler;

  /**
   * An additional thread for running inference so as not to block the camera.
   */
  private HandlerThread inferenceThread;

  /**
   * A {@link Handler} for running tasks in the background.
   */
  private Handler inferenceHandler;

  /**
   * An {@link ImageReader} that handles preview frame capture.
   */
  private ImageReader previewReader;

  /**
   * {@link android.hardware.camera2.CaptureRequest.Builder} for the camera preview
   */
  private CaptureRequest.Builder previewRequestBuilder;

  /**
   * {@link CaptureRequest} generated by {@link #previewRequestBuilder}
   */
  private CaptureRequest previewRequest;

  /**
   * A {@link Semaphore} to prevent the app from exiting before closing the camera.
   */
  private final Semaphore cameraOpenCloseLock = new Semaphore(1);

  /**
   * Shows a {@link Toast} on the UI thread.
   *
   * @param text The message to show
   */
  private void showToast(final String text) {
    final Activity activity = getActivity();
    if (activity != null) {
      activity.runOnUiThread(
          new Runnable() {
            @Override
            public void run() {
              Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
          });
    }
  }

  /**
   * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
   * width and height are at least as large as the respective requested values, and whose aspect
   * ratio matches with the specified value.
   *
   * @param choices     The list of sizes that the camera supports for the intended output class
   * @param width       The minimum desired width
   * @param height      The minimum desired height
   * @param aspectRatio The aspect ratio
   * @return The optimal {@code Size}, or an arbitrary one if none were big enough
   */
  private static Size chooseOptimalSize(
          final Size[] choices, final int width, final int height, final Size aspectRatio) {
    // Collect the supported resolutions that are at least as big as the preview Surface
    final List<Size> bigEnough = new ArrayList<Size>();
    for (final Size option : choices) {
      if (option.getHeight() >= MINIMUM_PREVIEW_SIZE && option.getWidth() >= MINIMUM_PREVIEW_SIZE) {
        LOGGER.i("Adding size: " + option.getWidth() + "x" + option.getHeight());
        bigEnough.add(option);
      } else {
        LOGGER.i("Not adding size: " + option.getWidth() + "x" + option.getHeight());
      }
    }

    // Pick the smallest of those, assuming we found any
    if (bigEnough.size() > 0) {
      final Size chosenSize = Collections.min(bigEnough, new CompareSizesByArea());
      LOGGER.i("Chosen size: " + chosenSize.getWidth() + "x" + chosenSize.getHeight());
      return chosenSize;
    } else {
      LOGGER.e("Couldn't find any suitable preview size");
      return choices[0];
    }
  }

  public static CameraConnectionFragment newInstance() {
    return new CameraConnectionFragment();
  }

  @Override
  public View onCreateView(
          final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {




    container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {


        hlist = new ArrayList<Item>();
        mContext = container.getContext();
        recyclerView = (RecyclerView) container.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        hlist.add(new Item(R.drawable.group_7, "나무"));
//        hlist.add(new Item(R.drawable.group_8, "꽃"));
//        hlist.add(new Item(R.drawable.group_9, "볼펜"));
//        hlist.add(new Item(R.drawable.group_13, "안경"));
//        hlist.add(new Item(R.drawable.group_14, "핸드폰"));
//        hlist.add(new Item(R.drawable.group_15, "종이"));
//        hlist.add(new Item(R.drawable.group_7, "나무"));
//        hlist.add(new Item(R.drawable.group_8, "꽃"));
//        hlist.add(new Item(R.drawable.group_9, "볼펜"));
//        hlist.add(new Item(R.drawable.group_13, "안경"));
//        hlist.add(new Item(R.drawable.group_14, "핸드폰"));
//        hlist.add(new Item(R.drawable.group_15, "종이"));
//        hlist.add(new Item(R.drawable.group_7, "나무"));
//        hlist.add(new Item(R.drawable.group_8, "꽃"));
//        hlist.add(new Item(R.drawable.group_9, "볼펜"));
//        hlist.add(new Item(R.drawable.group_13, "안경"));
//        hlist.add(new Item(R.drawable.group_14, "핸드폰"));
//        hlist.add(new Item(R.drawable.group_15, "종이"));

        itemAdapter = new SectionListDataAdapter(mContext, hlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);

        final GestureDetector gestureDetector = new GestureDetector(mContext,new GestureDetector.SimpleOnGestureListener()
        {
          @Override
          public boolean onSingleTapUp(MotionEvent e)
          {
            return true;
          }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
          String TAG = "LOG";
          @Override
          public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
          {
            Log.d(TAG,"onInterceptTouchEvent");
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child!=null&&gestureDetector.onTouchEvent(e))
            {
              Log.d(TAG, "getChildAdapterPosition=>" + rv.getChildAdapterPosition(child));
              Log.d(TAG,"getChildLayoutPosition=>"+rv.getChildLayoutPosition(child));
              Log.d(TAG,"getChildViewHolder=>" + rv.getChildViewHolder(child));

            }
            return false;
          }

          @Override
          public void onTouchEvent(RecyclerView rv, MotionEvent e)
          {
            Log.d(TAG,"onTouchEvent");

          }

          @Override
          public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
          {
            Log.d(TAG,"onRequestDisallowInterceptTouchEvent");
          }
        });






        container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        indicator = (Button) container.findViewById(R.id.indicator);
        item_list = (LinearLayout) container.findViewById(R.id.item_list);
        item_list.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        item_list_width = item_list.getMeasuredWidth();
//        parse_layout = (LinearLayout) container.findViewById(R.id.parse_list);

        Log.e("width", String.valueOf(item_list_width));

        sum = (Button) container.findViewById(R.id.sum);
        quest = (Button) container.findViewById(R.id.quest);

//        quest.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//            startActivity(new Intent(container.getContext(), UndrBarActivity.class));
//          }
//        });
        sum.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            startActivity(new Intent(container.getContext(), CombinationActivity.class));
          }
        });

        indicator.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            if(!isListOpen){
              animator = ObjectAnimator.ofFloat(item_list, "translationX", 0, item_list_width * 9 / 10);
              animator.setDuration(500);
              animator.start();
              isListOpen=true;
            }else{
              animator = ObjectAnimator.ofFloat(item_list, "translationX", item_list_width * 9/10 ,0);
              animator.setDuration(500);
              animator.start();
              isListOpen=false;
            }
          }
        });

      }
    });









    return inflater.inflate(R.layout.camera_connection_fragment, container, false);
  }

  @TargetApi(Build.VERSION_CODES.M)
  @Override
  public void onViewCreated(final View view, final Bundle savedInstanceState) {
    textureView = (AutoFitTextureView) view.findViewById(R.id.texture);
    scoreView = (RecognitionScoreView) view.findViewById(R.id.results);


    items_list = new ArrayList<>();


    resultAdapter = new ResultAdapter(items_list, getContext());


    scoreListView = (ListView) view.findViewById(R.id.resultsListView);
    scoreListView.setAdapter(resultAdapter);

    scoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hlist.add(new Item(items_list.get(position).img_src, items_list.get(position).name));
        itemAdapter.notifyDataSetChanged();
      }
    });
  }

  @Override
  public void onActivityCreated(final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    startBackgroundThread();

    // When the screen is turned off and turned back on, the SurfaceTexture is already
    // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
    // a camera and start preview from here (otherwise, we wait until the surface is ready in
    // the SurfaceTextureListener).
    if (textureView.isAvailable()) {
      openCamera(textureView.getWidth(), textureView.getHeight());
    } else {
      textureView.setSurfaceTextureListener(surfaceTextureListener);
    }
  }

  @Override
  public void onPause() {
    closeCamera();
    stopBackgroundThread();
    super.onPause();
  }

  /**
   * Sets up member variables related to camera.
   *
   * @param width  The width of available size for camera preview
   * @param height The height of available size for camera preview
   */
  private void setUpCameraOutputs(final int width, final int height) {
    final Activity activity = getActivity();
    final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    try {
      for (final String cameraId : manager.getCameraIdList()) {
        final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        // We don't use a front facing camera in this sample.
        final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
          continue;
        }

        final StreamConfigurationMap map =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        if (map == null) {
          continue;
        }

        // For still image captures, we use the largest available size.
        final Size largest =
            Collections.max(
//                Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)),
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                new CompareSizesByArea());

        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
        previewSize =
            chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, largest);
//        Log.e("CameraOutputs " , width + " x " + height);
//        Log.e("CameraOutputs preview" , previewSize.getWidth() + " x " + previewSize.getHeight());


        // We fit the aspect ratio of TextureView to the size of preview we picked.
        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//          textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
          textureView.setAspectRatio(width, height);
        } else {
//          textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
          textureView.setAspectRatio(height, width);
        }

        CameraConnectionFragment.this.cameraId = cameraId;
        return;
      }
    } catch (final CameraAccessException e) {
      LOGGER.e(e, "Exception!");
    } catch (final NullPointerException e) {
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      ErrorDialog.newInstance(getString(R.string.camera_error))
          .show(getChildFragmentManager(), FRAGMENT_DIALOG);
    }
  }

  /**
   * Opens the camera specified by {@link CameraConnectionFragment#cameraId}.
   */
  private void openCamera(final int width, final int height) {
    setUpCameraOutputs(width, height);
    configureTransform(width, height);
    final Activity activity = getActivity();
    final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    try {
      if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw new RuntimeException("Time out waiting to lock camera opening.");
      }
      manager.openCamera(cameraId, stateCallback, backgroundHandler);
    } catch (final CameraAccessException e) {
      LOGGER.e(e, "Exception!");
    } catch (final InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
    }
  }

  /**
   * Closes the current {@link CameraDevice}.
   */
  private void closeCamera() {
    try {
      cameraOpenCloseLock.acquire();
      if (null != captureSession) {
        captureSession.close();
        captureSession = null;
      }
      if (null != cameraDevice) {
        cameraDevice.close();
        cameraDevice = null;
      }
      if (null != previewReader) {
        previewReader.close();
        previewReader = null;
      }
    } catch (final InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
    } finally {
      cameraOpenCloseLock.release();
    }
  }

  /**
   * Starts a background thread and its {@link Handler}.
   */
  private void startBackgroundThread() {
    backgroundThread = new HandlerThread("ImageListener");
    backgroundThread.start();
    backgroundHandler = new Handler(backgroundThread.getLooper());

    inferenceThread = new HandlerThread("InferenceThread");
    inferenceThread.start();
    inferenceHandler = new Handler(inferenceThread.getLooper());
  }

  /**
   * Stops the background thread and its {@link Handler}.
   */
  private void stopBackgroundThread() {
    backgroundThread.quitSafely();
    inferenceThread.quitSafely();
    try {
      backgroundThread.join();
      backgroundThread = null;
      backgroundHandler = null;

      inferenceThread.join();
      inferenceThread = null;
      inferenceThread = null;
    } catch (final InterruptedException e) {
      LOGGER.e(e, "Exception!");
    }
  }

  private final TensorFlowImageListener tfPreviewListener = new TensorFlowImageListener();

  private final CameraCaptureSession.CaptureCallback captureCallback =
      new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(
            final CameraCaptureSession session,
            final CaptureRequest request,
            final CaptureResult partialResult) {}

        @Override
        public void onCaptureCompleted(
            final CameraCaptureSession session,
            final CaptureRequest request,
            final TotalCaptureResult result) {}
      };

  /**
   * Creates a new {@link CameraCaptureSession} for camera preview.
   */
  private void createCameraPreviewSession() {
    try {
      final SurfaceTexture texture = textureView.getSurfaceTexture();
      assert texture != null;

      // We configure the size of default buffer to be the size of camera preview we want.
      texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

      // This is the output Surface we need to start preview.
      final Surface surface = new Surface(texture);

      // We set up a CaptureRequest.Builder with the output Surface.
      previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
      previewRequestBuilder.addTarget(surface);

      LOGGER.i("Opening camera preview: " + previewSize.getWidth() + "x" + previewSize.getHeight());

      // Create the reader for the preview frames.
      previewReader =
          ImageReader.newInstance(
              previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);

      previewReader.setOnImageAvailableListener(tfPreviewListener, backgroundHandler);
      previewRequestBuilder.addTarget(previewReader.getSurface());

      // Here, we create a CameraCaptureSession for camera preview.
      cameraDevice.createCaptureSession(
          Arrays.asList(surface, previewReader.getSurface()),
          new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
              // The camera is already closed
              if (null == cameraDevice) {
                return;
              }

              // When the session is ready, we start displaying the preview.
              captureSession = cameraCaptureSession;
              try {
                // Auto focus should be continuous for camera preview.
                previewRequestBuilder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                // Flash is automatically enabled when necessary.
                previewRequestBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                // Finally, we start displaying the camera preview.
                previewRequest = previewRequestBuilder.build();
                captureSession.setRepeatingRequest(
                    previewRequest, captureCallback, backgroundHandler);
              } catch (final CameraAccessException e) {
                LOGGER.e(e, "Exception!");
              }
            }

            @Override
            public void onConfigureFailed(final CameraCaptureSession cameraCaptureSession) {
              showToast("Failed");
            }
          },
          null);
    } catch (final CameraAccessException e) {
      LOGGER.e(e, "Exception!");
    }

    LOGGER.i("Getting assets.");
    tfPreviewListener.initialize(
        getActivity().getAssets(), scoreView, inferenceHandler, sensorOrientation);
    LOGGER.i("TensorFlow initialized.");
  }

  /**
   * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
   * This method should be called after the camera preview size is determined in
   * setUpCameraOutputs and also the size of `mTextureView` is fixed.
   *
   * @param viewWidth  The width of `mTextureView`
   * @param viewHeight The height of `mTextureView`
   */
  private void configureTransform(final int viewWidth, final int viewHeight) {
    final Activity activity = getActivity();
    if (null == textureView || null == previewSize || null == activity) {
      return;
    }
    final int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    final Matrix matrix = new Matrix();
    final RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
    final RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
    final float centerX = viewRect.centerX();
    final float centerY = viewRect.centerY();
    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
      bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
      matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
      final float scale =
          Math.max(
              (float) viewHeight / previewSize.getHeight(),
              (float) viewWidth / previewSize.getWidth());
      matrix.postScale(scale, scale, centerX, centerY);
      matrix.postRotate(90 * (rotation - 2), centerX, centerY);
    } else if (Surface.ROTATION_180 == rotation) {
      matrix.postRotate(180, centerX, centerY);
    }
    textureView.setTransform(matrix);
  }

  /**
   * Compares two {@code Size}s based on their areas.
   */
  static class CompareSizesByArea implements Comparator<Size> {
    @Override
    public int compare(final Size lhs, final Size rhs) {
      // We cast here to ensure the multiplications won't overflow
      return Long.signum(
          (long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
    }
  }

  /**
   * Shows an error message dialog.
   */
  public static class ErrorDialog extends DialogFragment {
    private static final String ARG_MESSAGE = "message";

    public static ErrorDialog newInstance(final String message) {
      final ErrorDialog dialog = new ErrorDialog();
      final Bundle args = new Bundle();
      args.putString(ARG_MESSAGE, message);
      dialog.setArguments(args);
      return dialog;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
      final Activity activity = getActivity();
      return new AlertDialog.Builder(activity)
          .setMessage(getArguments().getString(ARG_MESSAGE))
          .setPositiveButton(
              android.R.string.ok,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, final int i) {
                  activity.finish();
                }
              })
          .create();
    }
  }




  class ResultAdapter extends BaseAdapter{
    ArrayList<Item> data_list;
    Context con;
    LayoutInflater inflater;
    ResultAdapter(ArrayList<Item> data_list, Context con){
      this.data_list = data_list;
      this.con = con;
      inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
      return data_list.size();
    }

    @Override
    public Item getItem(int position) {
      return data_list.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      convertView = inflater.inflate(R.layout.result_item, parent, false);
      TextView item_title = (TextView) convertView.findViewById(R.id.item_title);
      item_title.setText(data_list.get(position).name);

      return convertView;
    }
  }


  public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<Item> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<Item> itemsList) {
      this.itemsList = itemsList;
      this.mContext = context;
    }

    @Override
    public SectionListDataAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
      SectionListDataAdapter.SingleItemRowHolder mh = new SectionListDataAdapter.SingleItemRowHolder(v);
      return mh;
    }

    @Override
    public void onBindViewHolder(SectionListDataAdapter.SingleItemRowHolder holder, int i) {
      Item singleItem = itemsList.get(i);
      holder.title.setText(singleItem.getName());
      holder.image.setImageResource(singleItem.getImg());
    }

    @Override
    public int getItemCount() {
      return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
      protected TextView title;
      protected ImageView image;
      public SingleItemRowHolder(final View view) {
        super(view);
        this.title = (TextView) view.findViewById(R.id.textView);
        this.image = (ImageView) view.findViewById(R.id.imageView2);

        image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
//            if (Gloval.getElement1State()) {
//
//            } else if (Gloval.getElement2State()) {
//
//            } else {
              // 다이어로그 출력
              def_dialog = new DialogDefActivity(mContext,
                      "의자",
                      "나무",
                      "못",
                      "톱",
                      "의자(椅子)는 사람이 앉는데에 쓰는 가구이다.\n교상(交床)이라고도 한다.",
                      "2017.05.28 조합으로 획득");
              def_dialog.show();
           // }
          }
        });

      }
    }
  }
}
