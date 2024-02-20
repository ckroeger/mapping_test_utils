package io.github.ckroeger.mapping_test_utils.model.mapper;

import io.github.ckroeger.mapping_test_utils.model.source.ValueProperty;
import io.github.ckroeger.mapping_test_utils.model.source.ValueWithMaxPropery;
import io.github.ckroeger.mapping_test_utils.model.target.AddressType;
import io.github.ckroeger.mapping_test_utils.model.target.Country;
import io.github.ckroeger.mapping_test_utils.model.target.TargetAddress;
import io.github.ckroeger.mapping_test_utils.model.source.SourceAddress;

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
