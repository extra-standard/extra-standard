/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.drv.dsrv.spoc.commons.mtomdata;

/**
 * Definiert die Methoden fuer den Zugriff auf die Datenbanktabelle MTOM_DATA.
 */
public interface MtomDataDao {

	/**
	 * Fuegt den uebergebenen Eintrag in die Datenbanktabelle ein. <br />
	 * Es erfolgt keine weitere Pruefung der Feldinhalte; dazu bitte das Schema
	 * der Datenbank beachten.<br />
	 * <br />
	 * Beim Einfuegen eines Eintrags in die Datenbanktabelle MTOM_DATA wird von
	 * der Datenbanktabelle automatisch eine ID generiert. Diese ID wird im
	 * zugehoerigen Attribut innerhalb des uebergebenen Datenobjekts gespeichert
	 * und zusaetzlich von der Methode zurueckgegeben. Ausserdem wird das Feld
	 * ZEITPUNKT mit dem aktuellen Zeitpunkt gefuellt. Der Wert wird ebenfalls
	 * innerhalb des uebergebenen Datenobjekts gespeichert.
	 * 
	 * @param mtomData
	 *            der einzufuegende Eintrag. Hierin wird innerhalb der Methode
	 *            die beim Einfuegen des Eintrags automatisch von der Datenbank
	 *            generierte ID gespeichert
	 * @return automatisch generierte ID fuer den Eintrag
	 * @throws MtomDataDaoException
	 *             wenn beim Einfuegen des Eintrags ein technischer Fehler
	 *             auftritt
	 */
	int insert(MtomData mtomData) throws MtomDataDaoException;

	/**
	 * Ermittelt den Eintrag mit der angegebenen ID.<br/>
	 * Falls kein Eintrag zur angegebenen ID vorhanden ist, wird
	 * <code>null</code> zurueckgegeben.
	 * 
	 * @param id
	 *            die ID des zu ermittelnden Eintrags
	 * @return der Eintrag mit der angegebenen ID; <code>null</code>, falls kein
	 *         Eintrag vorhanden ist
	 * @throws MtomDataDaoException
	 *             wenn bei Ermittlung des Eintrags ein technischer Fehler
	 *             auftritt
	 */
	MtomData get(int id) throws MtomDataDaoException;

}
