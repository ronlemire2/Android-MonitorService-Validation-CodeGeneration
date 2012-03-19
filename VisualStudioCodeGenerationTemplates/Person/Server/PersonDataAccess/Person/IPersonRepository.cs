using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PersonBusinessEntities;

namespace PersonDataAccess
{
   public interface IPersonRepository
   {
      IEnumerable<Person> GetPersonList { get; }
      Person GetPerson(int Id);
      int SetPerson(Person entity);
      int DeletePerson(int Id);
   }
}