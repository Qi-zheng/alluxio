// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/sasl_server.proto

package alluxio.grpc;

public interface SaslMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:alluxio.grpc.sasl.SaslMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional .alluxio.grpc.sasl.SaslMessageType messageType = 1;</code>
   */
  boolean hasMessageType();
  /**
   * <code>optional .alluxio.grpc.sasl.SaslMessageType messageType = 1;</code>
   */
  alluxio.grpc.SaslMessageType getMessageType();

  /**
   * <code>optional bytes message = 2;</code>
   */
  boolean hasMessage();
  /**
   * <code>optional bytes message = 2;</code>
   */
  com.google.protobuf.ByteString getMessage();

  /**
   * <code>optional string clientId = 3;</code>
   */
  boolean hasClientId();
  /**
   * <code>optional string clientId = 3;</code>
   */
  java.lang.String getClientId();
  /**
   * <code>optional string clientId = 3;</code>
   */
  com.google.protobuf.ByteString
      getClientIdBytes();

  /**
   * <code>optional .alluxio.grpc.sasl.ChannelAuthenticationScheme authenticationScheme = 4;</code>
   */
  boolean hasAuthenticationScheme();
  /**
   * <code>optional .alluxio.grpc.sasl.ChannelAuthenticationScheme authenticationScheme = 4;</code>
   */
  alluxio.grpc.ChannelAuthenticationScheme getAuthenticationScheme();

  /**
   * <code>optional string channelRef = 5;</code>
   */
  boolean hasChannelRef();
  /**
   * <code>optional string channelRef = 5;</code>
   */
  java.lang.String getChannelRef();
  /**
   * <code>optional string channelRef = 5;</code>
   */
  com.google.protobuf.ByteString
      getChannelRefBytes();
}
