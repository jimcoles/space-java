/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.jnative.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.channels.SocketChannel;

/**
 * A fledgling attempt to create a Space wrapper over the Java Socket API.
 * @author Jim Coles
 */
public class Socket {

    public static void setSocketImplFactory(SocketImplFactory fac) throws IOException {
        java.net.Socket.setSocketImplFactory(fac);
    }
    private java.net.Socket jSocket;

    public void connect(SocketAddress endpoint) throws IOException {
        jSocket.connect(endpoint);
    }

    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        jSocket.connect(endpoint, timeout);
    }

    public void bind(SocketAddress bindpoint) throws IOException {
        jSocket.bind(bindpoint);
    }

    public InetAddress getInetAddress() {
        return jSocket.getInetAddress();
    }

    public InetAddress getLocalAddress() {
        return jSocket.getLocalAddress();
    }

    public int getPort() {
        return jSocket.getPort();
    }

    public int getLocalPort() {
        return jSocket.getLocalPort();
    }

    public SocketAddress getRemoteSocketAddress() {
        return jSocket.getRemoteSocketAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return jSocket.getLocalSocketAddress();
    }

    public SocketChannel getChannel() {
        return jSocket.getChannel();
    }

    public InputStream getInputStream() throws IOException {
        return jSocket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return jSocket.getOutputStream();
    }

    public boolean getTcpNoDelay() throws SocketException {
        return jSocket.getTcpNoDelay();
    }

    public void setTcpNoDelay(boolean on) throws SocketException {
        jSocket.setTcpNoDelay(on);
    }

    public void setSoLinger(boolean on, int linger) throws SocketException {
        jSocket.setSoLinger(on, linger);
    }

    public int getSoLinger() throws SocketException {
        return jSocket.getSoLinger();
    }

    public void sendUrgentData(int data) throws IOException {
        jSocket.sendUrgentData(data);
    }

    public boolean getOOBInline() throws SocketException {
        return jSocket.getOOBInline();
    }

    public void setOOBInline(boolean on) throws SocketException {
        jSocket.setOOBInline(on);
    }

    public int getSoTimeout() throws SocketException {
        return jSocket.getSoTimeout();
    }

    public void setSoTimeout(int timeout) throws SocketException {
        jSocket.setSoTimeout(timeout);
    }

    public int getSendBufferSize() throws SocketException {
        return jSocket.getSendBufferSize();
    }

    public void setSendBufferSize(int size) throws SocketException {
        jSocket.setSendBufferSize(size);
    }

    public int getReceiveBufferSize() throws SocketException {
        return jSocket.getReceiveBufferSize();
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        jSocket.setReceiveBufferSize(size);
    }

    public boolean getKeepAlive() throws SocketException {
        return jSocket.getKeepAlive();
    }

    public void setKeepAlive(boolean on) throws SocketException {
        jSocket.setKeepAlive(on);
    }

    public int getTrafficClass() throws SocketException {
        return jSocket.getTrafficClass();
    }

    public void setTrafficClass(int tc) throws SocketException {
        jSocket.setTrafficClass(tc);
    }

    public boolean getReuseAddress() throws SocketException {
        return jSocket.getReuseAddress();
    }

    public void setReuseAddress(boolean on) throws SocketException {
        jSocket.setReuseAddress(on);
    }

    public void close() throws IOException {
        jSocket.close();
    }

    public void shutdownInput() throws IOException {
        jSocket.shutdownInput();
    }

    public void shutdownOutput() throws IOException {
        jSocket.shutdownOutput();
    }

    public boolean isConnected() {
        return jSocket.isConnected();
    }

    public boolean isBound() {
        return jSocket.isBound();
    }

    public boolean isClosed() {
        return jSocket.isClosed();
    }

    public boolean isInputShutdown() {
        return jSocket.isInputShutdown();
    }

    public boolean isOutputShutdown() {
        return jSocket.isOutputShutdown();
    }

    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        jSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

}
