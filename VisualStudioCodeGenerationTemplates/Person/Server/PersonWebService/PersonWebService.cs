using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.ServiceModel.Channels;
using System.ServiceModel.Activation;
using System.ServiceModel.Description;
using PersonBusinessEntities;
using PersonBusinessLogic;

namespace PersonWebService
{
   [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
   [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
   public class PersonWebService : IPersonWebService
   {
      PersonBusinessLogic.IPersonService _personService = new PersonBusinessLogic.PersonService();
      
      public PersonList GetPersonList()
      {
         List<PersonView> pviewList =  _personService.GetPersonViewList();
         PersonList personList = new PersonList();

         foreach (PersonView pview in pviewList)
         {
            AddViewToList(pview, ref personList);
         }

         return personList;
      }

      public PersonList GetPerson(string Id)
      {
         PersonView pview = _personService.GetPersonView(int.Parse(Id));
         PersonList personList = new PersonList();
         AddViewToList(pview, ref personList);
         return personList;
      }

      private void AddViewToList(PersonView pview, ref PersonList list)
      {
         // Map PersonView to Person and add to PersonList
         list.Add(new Person()
         {
				Id = pview.Id,
				FirstName = pview.FirstName.Trim(),
				LastName = pview.LastName.Trim(),
				Email = pview.Email.Trim()
         });
      }

      public int SavePerson(string Id, Person addEditPerson)
      {
         int result = 0;

         PersonBusinessEntities.PersonView pview = new PersonBusinessEntities.PersonView();
			pview.Id = addEditPerson.Id;
			pview.FirstName = addEditPerson.FirstName.Trim();
			pview.LastName = addEditPerson.LastName.Trim();
			pview.Email = addEditPerson.Email.Trim();
         result = _personService.SavePerson(pview);

         return result;
      }

      public int DeletePerson(string Id)
      {
         int result = 0;
         result = _personService.DeletePerson(int.Parse(Id));
         return result;
      }
   }
}
