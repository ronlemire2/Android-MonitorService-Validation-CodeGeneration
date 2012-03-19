using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PersonBusinessEntities;
using PersonDataAccess;

namespace PersonBusinessLogic
{
   public class PersonService : ServiceBase, IPersonService
   {

      #region Constructors

      private IPersonRepository _repository;


      public PersonService()
         : this(new PersonRepository())
      {
      }

      public PersonService(IPersonRepository repository)
      {
         this._repository = repository;
      }

      #endregion

      /// <summary>
      /// 
      /// </summary>
      /// <returns></returns>
      public List<PersonView> GetPersonViewList()
      {
         List<PersonView> personViewList = new List<PersonView>();

         IEnumerable<Person> entityList = _repository.GetPersonList.OrderBy(d => d.Id);
         foreach (Person entity in entityList)
         {
            personViewList.Add(PersonMapper.MapEntityToView(entity));
         }

         return personViewList;
      }

      /// <summary>
      /// 
      /// </summary>
      /// <param name="Id"></param>
      /// <returns></returns>
      public PersonView GetPersonView(int Id)
      {
         PersonView personView = new PersonView();

         Person person = _repository.GetPerson(Id);
         personView = PersonMapper.MapEntityToView(person);

         return personView;
      }

      public static Person GetModel()
      {
         var entity = new Person();
         return entity;
      }

      /// <summary>
      /// 
      /// </summary>
      /// <param name="dtv"></param>
      /// <returns></returns>
      public int SavePerson(PersonView dtv)
      {
         int result = 0;

         // Validation
         this.Errors.Clear();

         try
         {
            // Data access
            if (!this.HasErrors)
            {
               // Save
               Person entity =  PersonMapper.MapViewToEntity(dtv);
               result = this._repository.SetPerson(entity);
            }
         }
         catch (System.Data.UpdateException ex)
         {
            if (ex.InnerException != null && ex.InnerException is System.Data.SqlClient.SqlException
               && ((System.Data.SqlClient.SqlException)ex.InnerException).ErrorCode == 8152)
               Errors.Add(ResourceStrings.ErrorMaxLength);
            else
               Errors.Add(ex.Message);
         }

         return result;
      }

      /// <summary>
      /// 
      /// </summary>
      /// <param name="Id"></param>
      /// <returns></returns>
      public int DeletePerson(int Id)
      {
         int result = 0;

         // Validation
         this.Errors.Clear();

         try
         {
            // Data access
            if (!this.HasErrors)
            {
               // Save
               result = this._repository.DeletePerson(Id);
            }
         }
         catch (System.Data.UpdateException ex)
         {
            if (ex.InnerException != null && ex.InnerException is System.Data.SqlClient.SqlException
               && ((System.Data.SqlClient.SqlException)ex.InnerException).ErrorCode == 8152)
               Errors.Add(ResourceStrings.ErrorMaxLength);
            else
                Errors.Add(ex.Message);
         }

         return result;
      }
   }
}
