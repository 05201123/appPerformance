/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\demo\\IPCTest\\src\\com\\jh\\ipctest\\binder\\IBookManager.aidl
 */
package com.jh.ipctest.binder.aidl;
/**
 * 是一个接口，有连个方法待实现，是我们自己定义的两个方法
 * 	getBookList（）
 *  addBook（）
 * @author 099
 *
 */
public interface IBookManager extends android.os.IInterface {
	/** Local-side IPC implementation stub class. */
	public static abstract class Stub extends android.os.Binder implements
			com.jh.ipctest.binder.aidl.IBookManager {
		//Binder唯一表示，一般是当前类名。
		private static final java.lang.String DESCRIPTOR = "com.jh.ipctest.binder.aidl.IBookManager";

		/** Construct the stub at attach it to the interface. */
		public Stub() {
			this.attachInterface(this, DESCRIPTOR);//将接口保存起来，用来asInterface方法判断，
										//如果是同进程的话，可以直接用，如果不是同进程，则需要IBookManager.Stub.Proxy(obj)
		}

		/**
		 * Cast an IBinder object into an com.jh.ipctest.binder.IBookManager
		 * interface, generating a proxy if needed.
		 */
		//***如果服务端 自己进程有客户端，其他进程也有客户端，会如何呢？？？？
		public static com.jh.ipctest.binder.aidl.IBookManager asInterface(
				android.os.IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);//取出上面创建attachInterface
			if (((iin != null) && (iin instanceof com.jh.ipctest.binder.IBookManager))) {//同进程
				return ((com.jh.ipctest.binder.aidl.IBookManager) iin);
			}
			return new com.jh.ipctest.binder.aidl.IBookManager.Stub.Proxy(obj);//非同进程
		}

		@Override
		public android.os.IBinder asBinder() {
			return this;
		}

		@Override
		/**
		 * 运行在服务器端Binder线程中，
		 */
		public boolean onTransact(int code, android.os.Parcel data,
				android.os.Parcel reply, int flags)
				throws android.os.RemoteException {
			switch (code) {
			case INTERFACE_TRANSACTION: {
				reply.writeString(DESCRIPTOR);
				return true;
			}
			case TRANSACTION_getBookList: {
				data.enforceInterface(DESCRIPTOR);
				java.util.List<com.jh.ipctest.binder.Book> _result = this
						.getBookList();
				reply.writeNoException();
				reply.writeTypedList(_result);
				return true;
			}
			case TRANSACTION_addBook: {
				data.enforceInterface(DESCRIPTOR);
				com.jh.ipctest.binder.Book _arg0;
				if ((0 != data.readInt())) {
					_arg0 = com.jh.ipctest.binder.Book.CREATOR
							.createFromParcel(data);
				} else {
					_arg0 = null;
				}
				this.addBook(_arg0);
				reply.writeNoException();
				return true;
			}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements
				com.jh.ipctest.binder.aidl.IBookManager {
			private android.os.IBinder mRemote;

			Proxy(android.os.IBinder remote) {
				mRemote = remote;
			}

			@Override
			public android.os.IBinder asBinder() {
				return mRemote;
			}

			public java.lang.String getInterfaceDescriptor() {
				return DESCRIPTOR;
			}

			@Override
			//运行在客户端
			public java.util.List<com.jh.ipctest.binder.Book> getBookList()
					throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				java.util.List<com.jh.ipctest.binder.Book> _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(com.jh.ipctest.binder.aidl.IBookManager.Stub.TRANSACTION_getBookList, _data,
							_reply, 0);
					_reply.readException();
					_result = _reply
							.createTypedArrayList(com.jh.ipctest.binder.Book.CREATOR);
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			@Override
			//运行在客户端
			public void addBook(com.jh.ipctest.binder.Book book)
					throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					if ((book != null)) {
						_data.writeInt(1);
						book.writeToParcel(_data, 0);
					} else {
						_data.writeInt(0);
					}
					mRemote.transact(com.jh.ipctest.binder.aidl.IBookManager.Stub.TRANSACTION_addBook, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}
		}
		//为两个方法设置常量值，以便在onTransact传递过程中，区分方法。
		static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
		static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
	}

	public java.util.List<com.jh.ipctest.binder.Book> getBookList()
			throws android.os.RemoteException;

	public void addBook(com.jh.ipctest.binder.Book book)
			throws android.os.RemoteException;
}
