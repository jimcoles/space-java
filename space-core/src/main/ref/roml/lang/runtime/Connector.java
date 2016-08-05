package com.jkc.roml.dms;

/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */

/**
 * The abstract base class defining basic behavior for obtaining a
 * Connection to a DataSystem regardless of the DataSystems sub-type.
 *
 * @author Jim Coles
 * @version 1.0
 */

public abstract class Connector
{

  abstract Connection connect(ConnectionInfo  connInfo)
    throws Exception;


}