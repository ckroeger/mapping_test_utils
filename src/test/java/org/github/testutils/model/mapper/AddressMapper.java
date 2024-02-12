package org.github.testutils.model.mapper;

import org.github.testutils.model.source.SourceAddress;
import org.github.testutils.model.source.ValueProperty;
import org.github.testutils.model.source.ValueWithMaxPropery;
import org.github.testutils.model.target.AddressType;
import org.github.testutils.model.target.Country;
import org.github.testutils.model.target.TargetAddress;

public class AddressMapper {

   public static TargetAddress mapToTargetAddress(SourceAddress source) {
      if (source == null) {
         return null;
      }

      TargetAddress target = new TargetAddress();
      target.setAddressType(new AddressType("RESIDENCE_ADDRESS"));
      target.setStreet(getValueIfPresent(source.getStreet()));
      target.setHouseNumber(getValueIfPresent(source.getHouseNumber()));
      target.setPoBox(getValueIfPresent(source.getPoBox()));
      target.setCity(getValueIfPresent(source.getCity()));
      target.setZipCode(getValueIfPresent(source.getPostalCode()));
      target.setCountry(new Country(getValueIfPresent(source.getCountry())));
      return target;
   }

   private static String getValueIfPresent(ValueWithMaxPropery valueWithMaxPropery) {
      return valueWithMaxPropery != null ? valueWithMaxPropery.getValue() : null;
   }
   private static String getValueIfPresent(ValueProperty valueWithMaxPropery) {
      return valueWithMaxPropery != null ? valueWithMaxPropery.getValue() : null;
   }
}
