/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.master.file.meta;

import alluxio.exception.InvalidPathException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Manages the locks for a list of {@link Inode}.
 */
@ThreadSafe
public final class InodeLockList implements AutoCloseable {
  private final List<Inode<?>> mInodes;
  private final List<InodeTree.LockMode> mLockModes;

  InodeLockList() {
    mInodes = new ArrayList<>();
    mLockModes = new ArrayList<>();
  }

  /**
   * Locks the given inode in read mode, and adds it to this lock list. This call should only be
   * used when locking the root or an inode by id and not path or parent.
   *
   * @param inode the inode to lock
   */
  public synchronized void lockRead(Inode<?> inode) {
    inode.lockRead();
    mInodes.add(inode);
    mLockModes.add(InodeTree.LockMode.READ);
  }

  /**
   * Locks the given inode in read mode, and adds it to this lock list. This method ensures the
   * parent is the expected parent inode.
   *
   * @param inode the inode to lock
   * @param parent the expected parent inode
   * @throws InvalidPathException if the inode is no long consistent with the caller's expectations
   */
  public synchronized void lockReadAndCheckParent(Inode<?> inode, Inode parent)
      throws InvalidPathException {
    inode.lockReadAndCheckParent(parent);
    mInodes.add(inode);
    mLockModes.add(InodeTree.LockMode.READ);
  }

  /**
   * Locks the given inode in read mode, and adds it to this lock list. This method ensures the
   * parent is the expected parent inode, and the name of the inode is the expected name.
   *
   * @param inode the inode to lock
   * @param parent the expected parent inode
   * @param name the expected name of the inode to be locked
   * @throws InvalidPathException if the inode is not consistent with the caller's expectations
   */
  public synchronized void lockReadAndCheckFullPath(Inode<?> inode, Inode parent, String name)
      throws InvalidPathException {
    inode.lockReadAndCheckFullPath(parent, name);
    mInodes.add(inode);
    mLockModes.add(InodeTree.LockMode.READ);
  }

  /**
   * Unlocks the last inode that was locked.
   */
  public synchronized void unlockLast() {
    if (mInodes.isEmpty()) {
      return;
    }
    Inode<?> inode = mInodes.remove(mInodes.size() - 1);
    InodeTree.LockMode lockMode = mLockModes.remove(mLockModes.size() - 1);
    if (lockMode == InodeTree.LockMode.READ) {
      inode.unlockRead();
    } else {
      inode.unlockWrite();
    }
  }

  /**
   * Locks the given inode in write mode, and adds it to this lock list. This call should only be
   * used when locking the root or an inode by id and not path or parent.
   *
   * @param inode the inode to lock
   */
  public synchronized void lockWrite(Inode<?> inode) {
    inode.lockWrite();
    mInodes.add(inode);
    mLockModes.add(InodeTree.LockMode.WRITE);
  }

  /**
   * Locks the given inode in write mode, and adds it to this lock list. This method ensures the
   * parent is the expected parent inode.
   *
   * @param inode the inode to lock
   * @param parent the expected parent inode
   * @throws InvalidPathException if the inode is not consistent with the caller's expectations
   */
  public synchronized void lockWriteAndCheckParent(Inode<?> inode, Inode parent)
      throws InvalidPathException {
    inode.lockWriteAndCheckParent(parent);
    mInodes.add(inode);
    mLockModes.add(InodeTree.LockMode.WRITE);
  }

  /**
   * Locks the given inode in write mode, and adds it to this lock list. This method ensures the
   * parent is the expected parent inode, and the name of the inode is the expected name.
   *
   * @param inode the inode to lock
   * @param parent the expected parent inode
   * @param name the expected name of the inode to be locked
   * @throws InvalidPathException if the inode is not consistent with the caller's expectations
   */
  public synchronized void lockWriteAndCheckFullPath(Inode<?> inode, Inode parent, String name)
      throws InvalidPathException {
    inode.lockWriteAndCheckFullPath(parent, name);
    mInodes.add(inode);
    mLockModes.add(InodeTree.LockMode.WRITE);
  }

  /**
   * @return the list of inodes locked in this lock list, in order of when the inodes were locked
   */
  public synchronized List<Inode<?>> getInodes() {
    return mInodes;
  }

  @Override
  public synchronized void close() {
    for (int i = mInodes.size() - 1; i >= 0; i--) {
      Inode<?> inode = mInodes.get(i);
      InodeTree.LockMode lockMode = mLockModes.get(i);
      if (lockMode == InodeTree.LockMode.READ) {
        inode.unlockRead();
      } else {
        inode.unlockWrite();
      }
    }
    mInodes.clear();
    mLockModes.clear();
  }
}
