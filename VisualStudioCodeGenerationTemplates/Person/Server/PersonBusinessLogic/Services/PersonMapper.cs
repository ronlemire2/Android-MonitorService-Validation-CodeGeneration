using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PersonBusinessEntities;

namespace PersonBusinessLogic
{
   public class PersonMapper
   {
      public static PersonView MapEntityToView(Person person)
      {
         PersonView personView = new PersonView();

			personView.Id = person.Id;
			personView.FirstName = person.FirstName.Trim();
			personView.LastName = person.LastName.Trim();
			personView.Email = person.Email.Trim();

         return personView;
      }

      public static Person MapViewToEntity(PersonView personView)
      {
         Person person = new Person();

			person.Id = personView.Id;
			person.FirstName = personView.FirstName;
			person.LastName = personView.LastName;
			person.Email = personView.Email;

         return person;
      }
   }
}


