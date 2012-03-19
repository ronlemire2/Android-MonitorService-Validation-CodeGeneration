using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PersonBusinessEntities;

namespace PersonBusinessLogic
{
   public interface IPersonService : IServiceBase
   {
      List<PersonView> GetPersonViewList();
      PersonView GetPersonView(int  Id);
      int SavePerson(PersonView personView);
      int DeletePerson(int Id);
   }
}